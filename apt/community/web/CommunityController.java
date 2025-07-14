package com.project.apt.community.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.apt.community.dao.ICommunityDAO;
import com.project.apt.community.dao.IReplyDAO;
import com.project.apt.community.service.CommunityService;
import com.project.apt.community.vo.CommunityVO;
import com.project.apt.community.vo.ReplyVO;
import com.project.apt.community.vo.ReplyWrapper;
import com.project.apt.user.vo.UserVO;

import jakarta.servlet.http.HttpSession;

@Controller
//@RequestMapping("/community")
public class CommunityController {
	@Autowired
    private ICommunityDAO communityDAO; // DAO 의존성 주입

	@Autowired
	private CommunityService communityService; // Service 의존성 주입 추가

	@Autowired
	private IReplyDAO replyDAO; // 댓글 DAO 의존성 주입
	
	
	// 커뮤니티 메인 페이지 (게시물 목록) 이동
    @GetMapping("/communityMain") // 이 URL로 요청이 오면 communityMain.html을 보여줍니다.
    public String communityMainMove( @RequestParam(value = "orderBy", defaultValue = "latest") String orderBy,
							    	 @RequestParam(value = "noticeOnly", defaultValue = "false") boolean noticeOnly,
							    	 Model model) {
    	
    		int offset = 0;
    	    int endRow = 3;

    	    Map<String, Object> paramMap = new HashMap<>();
    	    paramMap.put("offset", offset);
    	    paramMap.put("endRow", endRow);
    	    paramMap.put("orderBy", orderBy);
    	    
    	    if (noticeOnly) {
    	        paramMap.put("userId", "admin");
    	    }
    	    
    	    List<CommunityVO> postList = communityDAO.selectPostsByPage(paramMap);
    	    model.addAttribute("postList", postList);
    	    model.addAttribute("orderBy", orderBy);
    	    model.addAttribute("noticeOnly", noticeOnly);
    	    
        return "community/communityMain"; // communityMain.html 파일 경로
    }
    
    // 무한스크롤용 게시글 페이지 API
 	@GetMapping("/community/posts")
 	@ResponseBody
 	public List<CommunityVO> getPostsByPage(@RequestParam("page") int page,  
 			@RequestParam(value = "orderBy", defaultValue = "latest") String orderBy,
 			@RequestParam(value = "noticeOnly", defaultValue = "false") boolean noticeOnly) {
 		
 	    int pageSize   = 3;
 	    int offset = (page - 1) * pageSize ;
 	    int endRow = offset + pageSize;

 	    Map<String, Object> paramMap = new HashMap<>();
 	    paramMap.put("offset", offset);
 	    paramMap.put("endRow", endRow);
 	    paramMap.put("orderBy", orderBy);
 	    
 	   if (noticeOnly) {
 	        paramMap.put("userId", "admin"); // 공지사항만 가져오기
 	    }

 	    return communityDAO.selectPostsByPage(paramMap);
 	}

	// 게시글 상세 페이지 이동
	@GetMapping("/communityDetail")
	public String communityDetailMove(@RequestParam("boardNo") int boardNo, HttpSession session, Model model) {
		
        // 1. 게시글 정보 조회 (Service 계층 사용)
		CommunityVO post = communityService.getPostById(boardNo);

        if (post == null || "Y".equals(post.getIsDeleted())) { // 게시물이 없거나 삭제된 경우
            return "redirect:/communityMain";
        }
        model.addAttribute("post", post); // 조회된 게시물 정보를 모델에 담아 HTML로 전달

        // 2. 댓글 정보 조회
		/*
		 * List<ReplyVO> replies = communityService.getRepliesByBoardNo(boardNo);
		 * model.addAttribute("replies", replies != null ? replies : new ArrayList<>());
		 */
        List<ReplyVO> replyList = communityService.getRepliesByBoardNo(boardNo);
        
	     // 3. Flat → Tree 구조로 가공
	        List<ReplyWrapper> nestedReplies = new ArrayList<>();
	        Map<Integer, ReplyWrapper> replyMap = new HashMap<>();
	
	        for (ReplyVO reply : replyList) {
	            if (reply.getParentNo() == null) {
	                ReplyWrapper wrapper = new ReplyWrapper();
	                wrapper.setParent(reply);
	                nestedReplies.add(wrapper);
	                replyMap.put(reply.getReplyNo(), wrapper);
	            }
	        }
	
	        for (ReplyVO reply : replyList) {
	            if (reply.getParentNo() != null) {
	                ReplyWrapper parentWrapper = replyMap.get(reply.getParentNo());
	                if (parentWrapper != null) {
	                    parentWrapper.getChildren().add(reply);
	                }
	            }
	        }
	        
	        model.addAttribute("nestedReplies", nestedReplies);
        
        // 댓글 수 계산 (답글 포함 전체 수)
        int totalCommentCount = replyList.size();
        model.addAttribute("commentCount", totalCommentCount);
        
        // 현재 로그인한 사용자 ID를 모델에 추가
        String loggedInUserId = null;
        Object loginUserObj = session.getAttribute("loginUser"); // "loginUser" 키로 객체 가져오기
        if (loginUserObj instanceof UserVO) { // 가져온 객체가 UserVO 타입인지 확인
            UserVO loggedInUser = (UserVO) loginUserObj;
            loggedInUserId = loggedInUser.getUserId(); // UserVO 객체에서 실제 userId 필드 가져오기
        
        }
        model.addAttribute("loggedInUserId", loggedInUserId);

        return "community/communityDetail";
    }

	// 게시글 작성 페이지 이동
	@GetMapping("/communityWrite")
	public String communityWriteMove(HttpSession session, Model model) {
		UserVO user = (UserVO) session.getAttribute("loginUser");
        if (user == null) {
            // 로그인되어 있지 않으면 로그인 페이지로 리다이렉트
            return "redirect:/sign"; // 실제 로그인 페이지 경로에 맞게 수정
        }
        // 로그인 정보가 있다면, 폼으로 이동 (별도의 모델 데이터는 필요 없음)
        return "community/communityWrite";
	}
	
    // 게시물 등록 처리
    @PostMapping("/communityWrite") // 폼의 action과 일치해야 합니다.
    public String writePost(CommunityVO board, HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인되어 있지 않으면 로그인 페이지로 리다이렉트 (예외 처리)
            return "redirect:/sign"; // 실제 로그인 페이지 경로에 맞게 수정
        }

        // 로그인된 사용자의 ID를 BoardVO에 설정
        board.setUserId(user.getUserId());

        try {
        	communityDAO.insertPost(board);
            // 게시물 등록 성공 후 게시물 목록 페이지로 리다이렉트 (예시)
            return "redirect:/communityMain"; // 게시물 목록을 보여줄 URL로 변경해주세요.
        } catch (Exception e) {
            // 데이터베이스 저장 실패 시 오류 페이지 또는 메시지
            e.printStackTrace(); // 콘솔에 오류 로그 출력
            // model.addAttribute("errorMessage", "게시물 등록에 실패했습니다.");
            return "errorPage"; // 적절한 오류 페이지 이름으로 변경
        }
    }
 // 댓글 등록 처리
    @PostMapping("/community/addReply")
    @ResponseBody // 이 어노테이션을 사용하여 JSON 응답을 반환합니다.
    public Map<String, Object> addReply(@RequestParam("boardNo") int boardNo,
                                        @RequestParam("replyContent") String replyContent,
                                        @RequestParam(value = "parentNo", required = false) Integer parentNo, // 답글인 경우 parentNo를 받음 (필수 아님)
                                        HttpSession session) {
        Map<String, Object> response = new HashMap<>(); // 응답 메시지를 담을 맵

        UserVO user = (UserVO) session.getAttribute("loginUser");
        if (user == null) {
            response.put("status", "error");
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        ReplyVO reply = new ReplyVO();
        reply.setUserId(user.getUserId());
        reply.setBoardNo(boardNo);
        reply.setReplyContent(replyContent);
        reply.setParentNo(parentNo); // parentNo가 null이면 일반 댓글, 값이 있으면 답글

        try {
            int result = communityDAO.insertReply(reply);
            if (result > 0) {
                response.put("status", "success");
                response.put("message", "댓글이 성공적으로 등록되었습니다.");
                // 필요하다면 새로 등록된 댓글의 정보(replyNo, publishedDate 등)를 함께 반환할 수 있습니다.
                // response.put("reply", reply); 
            } else {
                response.put("status", "fail");
                response.put("message", "댓글 등록에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "서버 오류: " + e.getMessage());
        }
        return response; // JSON 형태로 응답 반환
    }
    
	// 내 활동 페이지 이동
	@GetMapping("/communityMy")
	public String communityMyMove(HttpSession session, Model model) {
		// 로그인 체크
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/sign"; // 로그인 페이지로 리다이렉트
		}
		
		model.addAttribute("loginUser", loginUser);
		return "community/communityMy";
	}
	
	// 내 게시글 조회 API
	@PostMapping("/community/getMyPosts")
	@ResponseBody
	public Map<String, Object> getMyPosts(HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			UserVO loginUser = (UserVO) session.getAttribute("loginUser");
			if (loginUser == null) {
				response.put("success", false);
				response.put("message", "로그인이 필요합니다.");
				return response;
			}
			
			// 내 게시글 조회
			List<CommunityVO> myPosts = communityService.getMyPosts(loginUser.getUserId());
			
			response.put("success", true);
			response.put("posts", myPosts);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "게시글 조회 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
		
		return response;
	}

	// 내 댓글 조회 API (실제 구현으로 수정)
	@PostMapping("/community/getMyComments")
	@ResponseBody
	public Map<String, Object> getMyComments(HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			UserVO loginUser = (UserVO) session.getAttribute("loginUser");
			if (loginUser == null) {
				response.put("success", false);
				response.put("message", "로그인이 필요합니다.");
				return response;
			}
			
			// 내 댓글 목록 조회 (실제 구현)
			List<ReplyVO> myReplies = replyDAO.selectMyReplies(loginUser.getUserId());
			
			response.put("success", true);
			response.put("comments", myReplies);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "댓글 조회 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
		
		return response;
	}

	// 게시글 삭제 API
	@PostMapping("/community/deletePost")
	@ResponseBody
	public Map<String, Object> deletePost(@RequestParam("boardNo") int boardNo, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			UserVO loginUser = (UserVO) session.getAttribute("loginUser");
			if (loginUser == null) {
				response.put("success", false);
				response.put("message", "로그인이 필요합니다.");
				return response;
			}
			
			// 게시글 작성자 확인
			CommunityVO post = communityService.getPostById(boardNo);
			if (post == null || !post.getUserId().equals(loginUser.getUserId())) {
				response.put("success", false);
				response.put("message", "삭제 권한이 없습니다.");
				return response;
			}
			
			// 게시글 논리 삭제 (is_deleted = 'Y')
			int result = communityService.deletePost(boardNo);
			
			if (result > 0) {
				response.put("success", true);
				response.put("message", "게시글이 삭제되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "삭제에 실패했습니다.");
			}
			
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "삭제 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
		
		return response;
	}

	// 댓글 삭제 API (새로 추가)
	@PostMapping("/community/deleteComment")
	@ResponseBody
	public Map<String, Object> deleteComment(@RequestParam("replyNo") int replyNo, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			UserVO loginUser = (UserVO) session.getAttribute("loginUser");
			if (loginUser == null) {
				response.put("success", false);
				response.put("message", "로그인이 필요합니다.");
				return response;
			}
			
			// 댓글 작성자 확인
			ReplyVO reply = replyDAO.selectReplyByNo(replyNo);
			if (reply == null || !reply.getUserId().equals(loginUser.getUserId())) {
				response.put("success", false);
				response.put("message", "삭제 권한이 없습니다.");
				return response;
			}
			
			// 댓글 논리 삭제
			int result = replyDAO.deleteReply(replyNo);
			
			if (result > 0) {
				response.put("success", true);
				response.put("message", "댓글이 삭제되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "삭제에 실패했습니다.");
			}
			
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "삭제 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
		
		return response;
	}
	
	// 댓글 수정 API
	@PostMapping("/community/updateReply/{replyNo}")
    @ResponseBody
    public Map<String, Object> updateReply(
        @PathVariable("replyNo") int replyNo,
        @RequestParam("replyContent") String replyContent,
        HttpSession session // 세션에서 사용자 ID를 가져오기 위함
    ) {
        Map<String, Object> response = new HashMap<>();

        String loggedInUserId = null;
        Object loginUserObj = session.getAttribute("loginUser");
        if (loginUserObj instanceof UserVO) { // UserVO는 사용자 로그인 정보를 담는 VO (UserVO는 직접 정의 필요)
            UserVO loggedInUser = (UserVO) loginUserObj;
            loggedInUserId = loggedInUser.getUserId();
        }

        if (loggedInUserId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        try {
            // 1. 해당 댓글의 작성자 ID를 가져와서 현재 로그인 사용자 ID와 비교
            ReplyVO existingReply = replyDAO.selectReplyByNo(replyNo);
            if (existingReply == null) {
                response.put("success", false);
                response.put("message", "존재하지 않는 댓글입니다.");
                return response;
            }
            if (!existingReply.getUserId().equals(loggedInUserId)) {
                response.put("success", false);
                response.put("message", "댓글을 수정할 권한이 없습니다.");
                return response;
            }

            // 2. ReplyVO 객체에 수정할 내용과 ID 설정
            ReplyVO replyToUpdate = new ReplyVO();
            replyToUpdate.setReplyNo(replyNo);
            replyToUpdate.setReplyContent(replyContent);
            replyToUpdate.setUserId(loggedInUserId); // DAO에서 작성자 확인용으로도 사용

            // 3. 서비스 호출하여 댓글 수정
            int result = communityService.updateReply(replyToUpdate);

            if (result > 0) {
                response.put("success", true);
                response.put("message", "댓글이 성공적으로 수정되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "댓글 수정에 실패했습니다.");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "서버 오류로 댓글 수정에 실패했습니다.");
            e.printStackTrace();
        }
        return response;
    }
	// 게시물 수정 내용 처리 API
    @PostMapping("/community/updatePost") // JavaScript의 fetch URL과 일치
    @ResponseBody // JSON 응답을 위해 필수
    public Map<String, Object> updatePost(
        @ModelAttribute CommunityVO board, // 수정된 내용을 담은 CommunityVO (boardNo, boardTitle, boardContent)
        HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();

        String loggedInUserId = null;
        Object loginUserObj = session.getAttribute("loginUser");
        if (loginUserObj instanceof UserVO) {
            UserVO loggedInUser = (UserVO) loginUserObj;
            loggedInUserId = loggedInUser.getUserId();
        }

        if (loggedInUserId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        try {
            // 보안을 위해 다시 한번 게시물 작성자 확인
            CommunityVO originalPost = communityDAO.selectPostByBoardNo(board.getBoardNo());
            if (originalPost == null) {
                response.put("success", false);
                response.put("message", "존재하지 않는 게시물입니다.");
                return response;
            }
            if (!originalPost.getUserId().equals(loggedInUserId)) {
                response.put("success", false);
                response.put("message", "게시물을 수정할 권한이 없습니다.");
                return response;
            }

            board.setUserId(loggedInUserId); // 현재 로그인한 사용자 ID를 VO에 설정 (쿼리에서 WHERE 조건으로 사용)
            int result = communityService.updatePost(board); // 게시물 수정 서비스 호출

            if (result > 0) {
                response.put("success", true);
                response.put("message", "게시물이 성공적으로 수정되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "게시물 수정에 실패했습니다.");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "서버 오류로 게시물 수정에 실패했습니다.");
            e.printStackTrace();
        }
        return response;
    }
	
	// A.P.T핑 페이지 이동
	@GetMapping("/communutyPingMain")
	public String communityPingMainMove() {
		return "community/communityPingMain";
	}
	
	// A.P.T핑 구독설정 팝업
	@GetMapping("/communitySubscribeSetting")
	public String communityPingSubMove() {
		return "community/communitySubscribeSetting";
	}
	
	// A.P.T핑 알림 보내기 페이지 이동
	@GetMapping("/communityPingSend")
	public String communityPingSendMove() {
		return "community/communityPingSend";
	}
	 // 신고 API 추가
    @PostMapping("/community/report")
    @ResponseBody
    public Map<String, Object> reportPost(@RequestParam("boardNo") int boardNo,
                                        @RequestParam(value = "reason", required = false) String reason,
                                        HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 로그인 확인 (선택사항)
            UserVO loginUser = (UserVO) session.getAttribute("loginUser");
            if (loginUser == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }
            
            // 신고 처리
            boolean success = communityService.reportBoard(boardNo);
            
            if (success) {
                response.put("success", true);
                response.put("message", "신고가 접수되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "신고 처리에 실패했습니다.");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "신고 처리 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        
        return response;
    }
}