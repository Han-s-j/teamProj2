package com.project.apt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	// 메인화면 이동
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	// 절약 챌린지 이동
	@GetMapping("/challengeMain")
	public String challengeMove() {
		return "challenge/challengeMain";
	}
	
	@GetMapping("/challengeDetail")
	public String challengeDetailMove() {
		return "challenge/challengeDetail";
	}
	
	// 관리비 매니저 이동
	@GetMapping("/feeMain")
	public String feeMainMove() {
		return "fee/feeMain";
	}
	
	// 관리비 ocr 결과/수정 페이지
	@GetMapping("/feeOcrResult")
	public String feeOcrResultMove() {
		return "fee/feeOcrResult";
	}
	
	// 로그인 회원가입 이동
	@GetMapping("/sign")
	public String signMove() {
		return "user/sign";
	}
	
	// 마이페이지 이동
	@GetMapping("/mypage")
	public String mypageMove() {
		return "redirect:/user/mypage";
	}
	
	// 설명 페이지 이동
	@GetMapping("/infoPage")
	public String infoPageMove() {
		return "common/infoPage";
	}
	
	// 커뮤니티 페이지 이동
//	@GetMapping("/communityMain")
//	public String communityMainMove() {
//		return "community/communityMain";
//	}
	
}
