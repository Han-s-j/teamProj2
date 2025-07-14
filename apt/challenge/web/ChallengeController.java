package com.project.apt.challenge.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import com.project.apt.challenge.service.ChallengeService;
import com.project.apt.user.vo.UserVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;
    
    @PostMapping("/challenge/earnPoints")
    @ResponseBody
    public Map<String, Object> earnPoints(@RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== 컨트롤러 시작 ===");
            System.out.println("받은 요청: " + request);
            
            // 세션에서 로그인 사용자 정보 가져오기
            UserVO user = (UserVO) session.getAttribute("loginUser");
            if (user == null) {
                System.out.println("사용자 세션이 없음");
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }
            
            String userId = user.getUserId();
            int points = 100;
            String challengeName = (String) request.get("challengeName");
            
            System.out.println("처리할 사용자: " + userId);
            System.out.println("챌린지명: " + challengeName);

            // 포인트 적립 처리 (반환 타입이 Map으로 변경됨)
            Map<String, Object> result = challengeService.addPoints(userId, points, challengeName);
            System.out.println("서비스 결과: " + result);
            
            // 서비스에서 반환된 결과를 그대로 클라이언트에 전달
            return result;
            
        } catch (Exception e) {
            System.err.println("컨트롤러에서 예외 발생: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "포인트 적립에 실패했습니다: " + e.getMessage());
            return response;
        }
    }
    
    @PostMapping("/challenge/getTodayCompleted")
    @ResponseBody
    public Map<String, Object> getTodayCompletedChallenges(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== 완료된 챌린지 조회 시작 ===");
            
            UserVO user = (UserVO) session.getAttribute("loginUser");
            if (user == null) {
                System.out.println("세션에 사용자 정보 없음");
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }
            
            String userId = user.getUserId();
            System.out.println("조회할 사용자 ID: " + userId);
            
            List<String> completedChallenges = challengeService.getTodayCompletedChallenges(userId);
            System.out.println("조회된 완료 챌린지 목록: " + completedChallenges);
            System.out.println("목록 크기: " + (completedChallenges != null ? completedChallenges.size() : "null"));
            
            response.put("success", true);
            response.put("completedChallenges", completedChallenges);
            System.out.println("응답 데이터: " + response);
            
        } catch (Exception e) {
            System.err.println("완료된 챌린지 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "데이터 조회에 실패했습니다: " + e.getMessage());
        }
        
        return response;
    }

    // 포인트 조회 - 챌린지 메인 페이지 (수정)
    @RequestMapping("/challengeMain")
    public String challengeMain(Model model, HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/sign";
        }
        
        try {
            int points = challengeService.getUserPoints(user.getUserId());
            System.out.println("챌린지메인 페이지 로드 시 사용자 포인트: " + points);
            model.addAttribute("userPoints", points);
            model.addAttribute("loginUser", user);
            model.addAttribute("loginUserId", user.getUserId()); // 추가
            return "challengeMain";
        } catch (Exception e) {
            System.err.println("챌린지메인 포인트 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("userPoints", 0);
            model.addAttribute("loginUser", user);
            model.addAttribute("loginUserId", user.getUserId()); // 추가
            return "challengeMain";
        }
    }
    
    // 챌린지 디테일 페이지 (수정)
    @RequestMapping("/challengeDetail")
    public String challengeDetail(Model model, HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/sign";
        }
        
        try {
            int points = challengeService.getUserPoints(user.getUserId());
            System.out.println("챌린지디테일 페이지 로드 시 사용자 포인트: " + points);
            
            // 디버깅 로그 추가
            System.out.println("Model에 추가할 데이터:");
            System.out.println("- loginUser: " + user);
            System.out.println("- userPoints: " + points);
            System.out.println("- loginUserId: " + user.getUserId());
            
            model.addAttribute("userPoints", points);
            model.addAttribute("loginUser", user);
            model.addAttribute("loginUserId", user.getUserId());
            
            return "challengeDetail";
        } catch (Exception e) {
            System.err.println("챌린지디테일 포인트 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("userPoints", 0);
            model.addAttribute("loginUser", user);
            model.addAttribute("loginUserId", user.getUserId());
            return "challengeDetail";
        }
    }
    
    // 테스트용 메서드 추가
    @GetMapping("/test/points")
    @ResponseBody
    public String testPoints(HttpSession session) {
        try {
            UserVO user = (UserVO) session.getAttribute("loginUser");
            if (user == null) {
                return "세션에 사용자 정보 없음";
            }
            
            String userId = user.getUserId();
            System.out.println("테스트 - 사용자 ID: " + userId);
            
            // 직접 DAO 호출 테스트
            int points = challengeService.getUserPoints(userId);
            
            return String.format("사용자: %s, 포인트: %d", userId, points);
            
        } catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }
    
    // 일일 완료 챌린지 수 조회 (달력용) - 뱃지는 제외하고 달력만
    @PostMapping("/challenge/getTodayCount")
    @ResponseBody
    public Map<String, Object> getTodayCount(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UserVO user = (UserVO) session.getAttribute("loginUser");
            if (user == null) {
                response.put("success", false);
                return response;
            }
            
            String userId = user.getUserId();
            int todayCount = challengeService.getTodayTotalCompletedCount(userId);
            
            response.put("success", true);
            response.put("todayCount", todayCount);
            response.put("isEightComplete", todayCount >= 8); // 8개 완료 시 달력에만 표시
            
        } catch (Exception e) {
            response.put("success", false);
        }
        
        return response;
    }

    @PostMapping("/challenge/getSessionInfo")
    @ResponseBody
    public Map<String, Object> getSessionInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UserVO user = (UserVO) session.getAttribute("loginUser");
            System.out.println("세션 정보 확인 - user: " + user);
            
            if (user != null) {
                int points = challengeService.getUserPoints(user.getUserId());
                System.out.println("사용자 포인트: " + points);
                
                response.put("success", true);
                response.put("loginUser", user);
                response.put("userId", user.getUserId());
                response.put("nickname", user.getNickname());
                response.put("userPoints", points);
            } else {
                response.put("success", false);
                response.put("message", "로그인 정보가 없습니다.");
            }
            
        } catch (Exception e) {
            System.err.println("세션 정보 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }
    
    // 월별 완료 날짜 조회 API 추가
    @PostMapping("/challenge/getMonthlyCompleted")
    @ResponseBody
    public Map<String, Object> getMonthlyCompleted(@RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UserVO user = (UserVO) session.getAttribute("loginUser");
            if (user == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다");
                return response;
            }
            
            int year = (Integer) request.get("year");
            int month = (Integer) request.get("month");
            
            // 서비스에서 해당 월의 완료 날짜 조회
            List<Integer> completedDates = challengeService.getMonthlyCompletedDates(
                user.getUserId(), year, month);
            
            response.put("success", true);
            response.put("completedDates", completedDates);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "조회 중 오류가 발생했습니다");
            e.printStackTrace();
        }
        
        return response;
    }

    // 포인트 내역 조회 API 추가
    @PostMapping("/challenge/getPointHistory")
    @ResponseBody
    public Map<String, Object> getPointHistory(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UserVO user = (UserVO) session.getAttribute("loginUser");
            if (user == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다");
                return response;
            }
            
            List<Map<String, Object>> pointHistory = challengeService.getPointHistory(user.getUserId());
            
            response.put("success", true);
            response.put("pointHistory", pointHistory);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "포인트 내역 조회 중 오류가 발생했습니다");
            e.printStackTrace();
        }
        
        return response;
    }
    
    // 뱃지 정보 조회 API 추가
    @PostMapping("/challenge/getUserBadges")
    @ResponseBody
    public Map<String, Object> getUserBadges(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UserVO user = (UserVO) session.getAttribute("loginUser");
            if (user == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다");
                return response;
            }
            
            List<Map<String, Object>> badges = challengeService.getUserBadgesWithInfo(user.getUserId());
            Map<String, Integer> progress = challengeService.getCategoryProgress(user.getUserId());
   
            
            // 오늘 새 뱃지가 있는지 확인
            boolean hasNewBadge = challengeService.checkTodayNewBadges(user.getUserId());
            
            response.put("success", true);
            response.put("badges", badges);
            response.put("progress", progress);
            response.put("hasNewBadge", hasNewBadge);
            response.put("newBadgeName", ""); // 실제로는 새 뱃지명을 가져와야 함
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "뱃지 정보 조회 중 오류가 발생했습니다");
            e.printStackTrace();
        }
        
        return response;
    }
    
    @PostMapping("/challenge/getCategoryProgress")
    @ResponseBody
    public Map<String, Object> getCategoryProgress(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UserVO user = (UserVO) session.getAttribute("loginUser");
            if (user == null) {
                response.put("success", false);
                return response;
            }
            
            Map<String, Integer> progress = challengeService.getCategoryProgress(user.getUserId());
            response.put("success", true);
            response.put("progress", progress);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    // 오늘 획득한 뱃지 확인 (새로 추가)
    @PostMapping("/challenge/checkTodayBadges")
    @ResponseBody
    public Map<String, Object> checkTodayBadges(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UserVO user = (UserVO) session.getAttribute("loginUser");
            if (user == null) {
                response.put("success", false);
                return response;
            }
            
            // 오늘 획득한 뱃지가 있는지 확인
            boolean hasNewBadge = challengeService.checkTodayNewBadges(user.getUserId());
            
            response.put("success", true);
            response.put("hasNewBadge", hasNewBadge);
            
        } catch (Exception e) {
            response.put("success", false);
            e.printStackTrace();
        }
        
        return response;
    }
    
    // 절약 랭킹 조회 API 추가
    @PostMapping("/challenge/getRanking")
    @ResponseBody
    public Map<String, Object> getRanking() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> ranking = challengeService.getTopRanking();
            response.put("success", true);
            response.put("ranking", ranking);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "랭킹 조회 중 오류가 발생했습니다");
            e.printStackTrace();
        }
        
        return response;
    }
}