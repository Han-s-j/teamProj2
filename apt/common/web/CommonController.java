package com.project.apt.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.apt.common.service.CommonService;
import com.project.apt.user.vo.UserAdminVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommonController {

	@Autowired
	CommonService service;
	
	// 유저 확인
    private boolean isAdmin(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return userId != null && userId.equals("admin");
    }

    // 관리자 메인 페이지
    @GetMapping("/admin/main")
    public String adminMainMove(HttpSession session) {
        if (!isAdmin(session)) {
            return "common/accessDenied";
        }
        return "common/adminMain";
    }
	
    // 유저 목록
    @GetMapping("/admin/users")
    public String userList(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "common/accessDenied";
        }
        
        List<UserAdminVO> userList = service.getAllUsers();
        model.addAttribute("userList", userList);
        
        return "common/adminUser";
    }

    // 커뮤니티 관리
    @GetMapping("/admin/community")
    public String communityList(HttpSession session) {
        if (!isAdmin(session)) {
            return "common/accessDenied";
        }
        return "common/adminCommunity";
    }

    // 통계 페이지 (방문자와 차트 포함)
    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "common/accessDenied";
        }
        
        // 기존에 잘 되던 기본 데이터
        model.addAttribute("userCount", service.getAllUserCount());
        model.addAttribute("postCount", service.getAllBoardCount());
        model.addAttribute("pointSum", service.getUserPoint());
        
        // 오늘 통계 데이터 (실제 DB에서 조회)
        model.addAttribute("todayVisitors", service.getTodayVisitors());
        model.addAttribute("todaySignups", service.getTodaySignups());
        model.addAttribute("todayWithdrawals", service.getTodayWithdrawals());
        
        // 차트 데이터 (실제 DB에서 조회)
        model.addAttribute("visitorsChartData", service.getVisitorsChartData());
        model.addAttribute("userChangeChartData", service.getUserChangeChartData());
        
        return "common/adminDashboard";
    }
}