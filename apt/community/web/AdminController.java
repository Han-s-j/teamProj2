package com.project.apt.community.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.apt.community.service.CommunityService;
import com.project.apt.community.vo.CommunityVO;

@Controller
public class AdminController {
    
    @Autowired
    private CommunityService communityService;
    
    // 신고된 게시글 목록 API
    @GetMapping("/admin/community/reported")
    @ResponseBody
    public Map<String, Object> getReportedPosts() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<CommunityVO> reportedPosts = communityService.getReportedPosts();
            response.put("success", true);
            response.put("posts", reportedPosts);
            
            // 디버깅을 위한 로그 추가
            System.out.println("신고된 게시글 수: " + (reportedPosts != null ? reportedPosts.size() : 0));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "데이터 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        
        return response;
    }
    
    // 관리자 게시글 삭제 API
    @PostMapping("/admin/community/delete")
    @ResponseBody
    public Map<String, Object> adminDeletePost(@RequestParam("boardNo") int boardNo) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("삭제 요청 받은 게시글 번호: " + boardNo);
            
            boolean success = communityService.adminDeletePost(boardNo);
            
            System.out.println("삭제 결과: " + success);
            
            if (success) {
                response.put("success", true);
                response.put("message", "게시글이 성공적으로 삭제되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "게시글 삭제에 실패했습니다. 게시글이 존재하지 않거나 이미 삭제된 상태일 수 있습니다.");
            }
            
        } catch (Exception e) {
            System.err.println("게시글 삭제 중 오류 발생: " + e.getMessage());
            response.put("success", false);
            response.put("message", "삭제 처리 중 서버 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
    // 신고 해제 API
    @PostMapping("/admin/community/unreport")
    @ResponseBody
    public Map<String, Object> unreportPost(@RequestParam("boardNo") int boardNo) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("신고해제 요청 받은 게시글 번호: " + boardNo);
            
            boolean success = communityService.unreportPost(boardNo);
            
            System.out.println("신고해제 결과: " + success);
            
            if (success) {
                response.put("success", true);
                response.put("message", "신고가 성공적으로 해제되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "신고 해제에 실패했습니다. 게시글이 존재하지 않거나 이미 처리된 상태일 수 있습니다.");
            }
            
        } catch (Exception e) {
            System.err.println("신고해제 중 오류 발생: " + e.getMessage());
            response.put("success", false);
            response.put("message", "신고해제 처리 중 서버 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
    // 테스트용 API
    @GetMapping("/admin/test")
    @ResponseBody
    public Map<String, Object> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "관리자 API가 정상 작동합니다.");
        return response;
    }

    // 더 간단한 신고된 게시글 테스트 API
    @GetMapping("/admin/community/test-reported")
    @ResponseBody
    public Map<String, Object> testReportedPosts() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // CommunityService 주입 확인
            if (communityService == null) {
                response.put("success", false);
                response.put("message", "CommunityService가 주입되지 않았습니다.");
                return response;
            }
            
            // 메서드 호출 테스트
            List<CommunityVO> reportedPosts = communityService.getReportedPosts();
            
            response.put("success", true);
            response.put("message", "테스트 성공");
            response.put("count", reportedPosts != null ? reportedPosts.size() : 0);
            response.put("posts", reportedPosts);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "오류: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
}