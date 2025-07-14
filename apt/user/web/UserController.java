package com.project.apt.user.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.apt.user.service.UserService;
import com.project.apt.user.vo.OcrResultVO;
import com.project.apt.user.vo.UserUpdateVO;
import com.project.apt.user.vo.UserVO;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired // RestTemplate Bean 주입 (SpringApplication 클래스에 @Bean으로 등록 필요)
    private RestTemplate restTemplate;

	// 회원가입 처리
	@PostMapping("/signup")
	public String signup(UserVO vo, RedirectAttributes redirectAttributes) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		vo.setUserPw(encoder.encode(vo.getUserPw()));

		boolean signupSuccess = userService.register(vo);
		if (signupSuccess) {
			redirectAttributes.addFlashAttribute("signupSuccess", "회원가입이 완료되었습니다. 로그인해주세요.");
			return "redirect:/sign"; // 회원가입 성공 후 로그인 페이지로 리다이렉트
		} else {
			redirectAttributes.addFlashAttribute("signupError", "회원가입 실패. 다시 시도해주세요.");
			return "redirect:/signup"; // 회원가입 실패시 다시 회원가입 페이지로 리디렉트
		}
	}

	// 로그인 처리
	@PostMapping("/login")
	public String login(@RequestParam String userId, @RequestParam String userPw, HttpSession session,
			RedirectAttributes redirectAttributes) {
		UserVO user = userService.login(userId, userPw); // 로그인 서비스 호출

		if (user != null) {
			session.setAttribute("loginUser", user); // 로그인한 사용자 세션에 저장
			// 06.30 관리자 추가
			session.setAttribute("userId", user.getUserId()); // 관리자 체크용
			return "redirect:/"; // 로그인 성공 후 홈 페이지로 리디렉션
		} else {
			redirectAttributes.addFlashAttribute("loginError", "아이디 또는 비밀번호가 틀렸습니다.");
			return "redirect:/sign"; // 로그인 실패시 다시 로그인 페이지로 리디렉트
		}
	}

	// 로그아웃 처리
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
		// 세션 무효화
		session.invalidate();
		// 로그아웃 후 홈으로 리디렉션
		redirectAttributes.addFlashAttribute("logoutSuccess", "로그아웃 되었습니다.");
		return "redirect:/"; // 홈 페이지로 리디렉션
	}

	// 탈퇴하기 처리 (계정 삭제)
	@GetMapping("/deleteAccount")
	public String deleteAccount(HttpSession session, RedirectAttributes redirectAttributes) {
		// 로그인된 사용자 정보 가져오기
		UserVO user = (UserVO) session.getAttribute("loginUser");

		if (user != null) {
			try {
				// 계정 삭제
				userService.deleteUser(user.getUserId());

				// 세션 무효화 (로그아웃 처리)
				session.invalidate();

				// 로그아웃 후 홈 페이지로 리디렉션
				redirectAttributes.addFlashAttribute("logoutSuccess", "회원 탈퇴가 완료되었습니다.");
				return "redirect:/"; // 홈 화면으로 리디렉션
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("error", "계정 삭제 중 오류가 발생했습니다.");
				return "redirect:/mypage"; // 오류 시 마이페이지로 리디렉션
			}
		} else {
			return "redirect:/login"; // 로그인되지 않은 사용자일 경우 로그인 페이지로 리디렉션
		}
	}

	// 아파트 인증 페이지 이동
	@GetMapping("/myAptAuth")
	public String myAptAuthMove() {
		return "user/myAptAuth";
	}
	
	// 마이페이지 진입 시 사용자 정보 갱신
	@GetMapping("/user/mypage")
	public String myPage(HttpSession session, Model model) {
	    UserVO loginUser = (UserVO) session.getAttribute("loginUser");
	    if (loginUser != null) {
	        // 기존 사용자 정보 갱신
	        UserVO updatedUser = userService.getUserById(loginUser.getUserId());
	        if (updatedUser != null) {
	            session.setAttribute("loginUser", updatedUser);
	        }
	        
	        // 구독 정보 조회 추가
	        Map<String, Object> subscriptionInfo = userService.getUserSubscriptionInfo(loginUser.getUserId());
	        boolean isSubscribed = false;
	        if (subscriptionInfo != null && "Y".equals(subscriptionInfo.get("communitySubscribed"))) {
	            isSubscribed = true;
	        }
	        
	        model.addAttribute("communitySubscribed", isSubscribed);
	        model.addAttribute("communitySubscribeDate", subscriptionInfo != null ? subscriptionInfo.get("communitySubscribeDate") : null);
	    }
	    return "user/mypage";
	}
	// 1단계: 고지서 업로드 -> OCR 결과 및 아파트명 확인 후 클라이언트에 반환 (DB 저장 없음)
	 @PostMapping("/aptAuthProcess")
	 @ResponseBody // 이 메서드는 JSON 데이터를 직접 반환
	 public ResponseEntity<?> aptAuthProcess(@RequestParam MultipartFile file, HttpSession session) {
	        // 로그인된 사용자 정보 확인
	        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
	        if (loginUser == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("로그인이 필요합니다."));
	        }

	        if (file.isEmpty()) {
	            return ResponseEntity.badRequest().body(createErrorResponse("파일이 없습니다."));
	        }

	        try {
	            // 1. Python OCR 서버로 파일 전달
	            String pythonOcrUrl = "http://192.168.0.35:5000/ocr/auth"; // Python OCR 서버 URL

	            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	            body.add("file", file.getResource());

	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

	            // Python 서버로부터 OCR 결과 받기
	            ResponseEntity<OcrResultVO> pythonResponse = restTemplate.postForEntity(pythonOcrUrl, requestEntity, OcrResultVO.class);

	            if (!pythonResponse.getStatusCode().is2xxSuccessful() || pythonResponse.getBody() == null) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("OCR 서버 응답 오류 또는 빈 응답입니다."));
	            }

	            OcrResultVO ocrResult = pythonResponse.getBody();

	            // OCR 결과의 필수 필드 확인
	            if (ocrResult.getDong_result() == null || ocrResult.getHo_result() == null 
	            		|| ocrResult.getNumber_result() == null) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                		.body(createErrorResponse("OCR 결과에 필수 정보(동, 호, 전화번호)가 누락되었습니다."));
	            }

	            // 2. 관리소 전화번호로 kapt_code 조회
	            String managementOfficeTel = ocrResult.getNumber_result();
	            String kaptCode = userService.findKaptCodeByManagementOfficeTel(managementOfficeTel); // UserService를 통해 DAO 호출
	            String aptName = null;
	            // kapt_code를 찾지 못했을 경우의 처리 (필요에 따라 에러 반환 또는 null 허용)
	            if (kaptCode != null) {
					// 3. kapt_code로 아파트명 조회 (새로운 DAO/Service 메서드 필요)
					aptName = userService.findAptNameByKaptCode(kaptCode); // 이 메서드를 UserService에 추가해야 함!
				} else {
					// kapt_code를 찾지 못했으니 aptName도 없음
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(createErrorResponse("해당 관리소 전화번호로 아파트 정보를 찾을 수 없습니다."));
				}
	            
	         // 4. 클라이언트에 반환할 응답 구성 (DB 저장 없음)
				Map<String, Object> response = new HashMap<>();
				response.put("message", ocrResult.getMessage()); // OCR 결과 (동, 호, 전화번호)
				response.put("image", ocrResult.getImage()); // Base64 이미지
				response.put("aptName", aptName); // 아파트명 추가
				response.put("kaptCode", kaptCode); // KaptCode 추가 (클라이언트가 '확인' 시 다시 보낼 값)

				return ResponseEntity.ok(response);

			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("서버 내부 오류: " + e.getMessage()));
			}
		}
	// 2단계: 클라이언트가 '확인' 버튼 클릭 시 실제 DB 저장
		@PostMapping("/saveAptAuthInfo")
		@ResponseBody // JSON 데이터를 받아서 처리하고, JSON 응답을 반환
		@Transactional // DB 트랜잭션 관리
		public ResponseEntity<?> saveAptAuthInfo(@RequestBody Map<String, String> authData, HttpSession session) {
			UserVO loginUser = (UserVO) session.getAttribute("loginUser");
			if (loginUser == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("로그인이 필요합니다."));
			}

			// 클라이언트로부터 전송된 데이터 추출
			String dong = authData.get("dong");
			String ho = authData.get("ho");
			String kaptCode = authData.get("kaptCode"); // 클라이언트에서 넘겨받은 kaptCode 사용

			// 필수 값 검증
			if (dong == null || ho == null || kaptCode == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("필수 인증 정보가 누락되었습니다."));
			}

			try {
				// 호(ho) 값으로 층수(userFloor) 계산
				Integer userFloor = null;
				if (!ho.isEmpty()) {
					String floorStr;
					if (ho.length() == 3) {
						floorStr = ho.substring(0, 1);
					} else if (ho.length() >= 4) {
						floorStr = ho.substring(0, 2);
					} else {
						floorStr = ho;
					}
					try {
						userFloor = Integer.parseInt(floorStr);
					} catch (NumberFormatException e) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("호(Ho) 정보에서 층수를 파악할 수 없습니다: " + floorStr));
					}
				}

				// UserUpdateVO 생성
				UserUpdateVO userUpdateVo = new UserUpdateVO();
				userUpdateVo.setUserId(loginUser.getUserId());
				
				// 동 값에 "동" 문자열 추가
				if (dong != null && !dong.isEmpty()) {
					userUpdateVo.setUserBlock(dong + "동");
				} else {
					userUpdateVo.setUserBlock(null); // 동 정보가 없으면 null로 저장 
				}
				
				userUpdateVo.setUserFloor(userFloor);
				userUpdateVo.setKaptCode(kaptCode); // 클라이언트에서 받은 kaptCode 사용

				// DB에 사용자 정보 업데이트
				userService.updateUserInfo(userUpdateVo);

				// 세션에 로그인 사용자 정보 업데이트 (새로운 동, 호, 아파트 코드 반영)
				loginUser.setUserBlock(userUpdateVo.getUserBlock());
				loginUser.setUserFloor(userUpdateVo.getUserFloor());
				loginUser.setKaptCode(userUpdateVo.getKaptCode());
				session.setAttribute("loginUser", loginUser); // 세션 정보 갱신

				Map<String, String> successResponse = new HashMap<>();
				successResponse.put("message", "아파트 인증 정보가 성공적으로 저장되었습니다.");
				return ResponseEntity.ok(successResponse);

			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("인증 정보 저장 중 서버 오류: " + e.getMessage()));
			}
		}

		// 에러 응답을 위한 헬퍼 메서드
		private Map<String, String> createErrorResponse(String errorMessage) {
			Map<String, String> error = new HashMap<>();
			error.put("error", errorMessage);
			return error;
		}
	         
		// 개인정보 수정 페이지 이동
		@GetMapping("/myEdit")
		public String myEditMove() {
			return "user/myEdit";
		}
}
