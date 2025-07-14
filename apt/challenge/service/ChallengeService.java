package com.project.apt.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.apt.challenge.dao.IChallengeDAO;
import com.project.apt.challenge.dao.BadgeDAO;
import com.project.apt.challenge.vo.UserBadgeVO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Service
public class ChallengeService {
    
    @Autowired
    private IChallengeDAO challengeDAO;
    
    @Autowired
    private BadgeDAO badgeDAO;
    
    @Transactional
    public Map<String, Object> addPoints(String userId, int points, String challengeName) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        
        try {
            System.out.println("=== 포인트 적립 시작 ===");
            System.out.println("userId: " + userId);
            System.out.println("points: " + points);
            System.out.println("challengeName: " + challengeName);
            
            Date today = Date.valueOf(LocalDate.now());
            System.out.println("오늘 날짜: " + today);
            
            // 오늘 이미 해당 챌린지를 완료했는지 확인
            int completedCount = challengeDAO.checkChallengeCompletedToday(userId, challengeName, today);
            System.out.println("완료된 챌린지 개수: " + completedCount);
            
            if (completedCount > 0) {
                System.out.println("오늘 이미 완료한 챌린지입니다: " + challengeName);
                result.put("alreadyCompleted", true);
                result.put("message", "오늘 이미 완료한 챌린지입니다.");
                return result;
            }

            // 1. 포인트 기록 저장
            System.out.println("포인트 히스토리 저장 시도...");
            challengeDAO.savePointHistory(userId, points, challengeName, new java.util.Date());
            System.out.println("포인트 히스토리 저장 완료");

            // 2. 총 포인트 업데이트
            System.out.println("총 포인트 업데이트 시도...");
            int beforePoints = challengeDAO.getUserPoints(userId);
            System.out.println("업데이트 전 포인트: " + beforePoints);
            
            challengeDAO.updateTotalPoint(userId, points);
            System.out.println("총 포인트 업데이트 완료");
            
            int afterPoints = challengeDAO.getUserPoints(userId);
            System.out.println("업데이트 후 포인트: " + afterPoints);

            // 3. 뱃지 체크 및 부여
            Map<String, Object> badgeResult = checkAndAwardBadges(userId, challengeName);
            
            result.put("success", true);
            result.put("totalPoints", afterPoints);
            
            // 새 뱃지가 있으면 결과에 포함
            if ((Boolean) badgeResult.get("newBadgeEarned")) {
                result.put("newBadge", badgeResult);
            }
            
            System.out.println("=== 포인트 적립 완료 ===");
            return result;

        } catch (Exception e) {
            System.err.println("포인트 적립 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // 뱃지 체크 및 부여 로직 (개선된 버전)
    private Map<String, Object> checkAndAwardBadges(String userId, String challengeName) {
        Map<String, Object> badgeResult = new HashMap<>();
        badgeResult.put("newBadgeEarned", false);
        
        try {
            System.out.println("=== 뱃지 체크 시작 ===");
            
            // 카테고리별 뱃지 체크
            String category = challengeName.split(" - ")[0]; // "물", "전기", "자원" 추출
            
            // 해당 카테고리의 서로 다른 챌린지 5개 완료 확인
            int uniqueCategoryCount = challengeDAO.getUniqueCategoryCompletionCount(userId, category);
            System.out.println(category + " 카테고리 고유 챌린지 완료 개수: " + uniqueCategoryCount);
            
            // 카테고리별 뱃지 (5개 완료)
            if (uniqueCategoryCount >= 5) {
                String badgeId = "";
                String badgeName = "";
                switch (category) {
                    case "물":
                        badgeId = "water_badge01";
                        badgeName = "물 절약 초심자";
                        break;
                    case "전기":
                        badgeId = "energy_badge01";
                        badgeName = "에너지 절약 초심자";
                        break;
                    case "자원":
                        badgeId = "waste_badge01";
                        badgeName = "자원 절약 초심자";
                        break;
                }
                
                if (!badgeId.isEmpty() && !badgeDAO.hasBadge(userId, badgeId)) {
                    badgeDAO.awardBadge(userId, badgeId);
                    System.out.println(category + " 절약 초심자 뱃지 부여! (badgeId: " + badgeId + ")");
                    
                    // 새 뱃지 정보 반환 (수정된 부분)
                    badgeResult.put("newBadgeEarned", true);
                    badgeResult.put("badgeId", badgeId);
                    badgeResult.put("badgeName", badgeName);
                    badgeResult.put("badgeImage", "/badge/image/" + badgeId); // ✅ 올바른 경로
                    badgeResult.put("category", category);
                }
            }
            
            System.out.println("=== 뱃지 체크 완료 ===");
            
        } catch (Exception e) {
            System.err.println("뱃지 처리 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
        
        return badgeResult;
    }

    public int getUserPoints(String userId) {
        try {
            System.out.println("포인트 조회 시작 - 사용자: " + userId);
            int points = challengeDAO.getUserPoints(userId);
            System.out.println("조회된 포인트: " + points);
            return points;
        } catch (Exception e) {
            System.err.println("포인트 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // 오늘 완료한 챌린지 목록 조회
    public List<String> getTodayCompletedChallenges(String userId) {
        try {
            Date today = Date.valueOf(LocalDate.now());
            System.out.println("완료된 챌린지 조회 - 사용자: " + userId + ", 날짜: " + today);
            
            List<String> result = challengeDAO.getTodayCompletedChallenges(userId, today);
            System.out.println("DAO에서 조회된 결과: " + result);
            
            return result;
        } catch (Exception e) {
            System.err.println("getTodayCompletedChallenges 오류: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // 사용자 뱃지 목록 조회 (JOIN 버전으로 개선)
    public List<Map<String, Object>> getUserBadgesWithInfo(String userId) {
        try {
            System.out.println("사용자 뱃지 상세 조회 (JOIN) - 사용자: " + userId);
            
            // JOIN 쿼리 사용 - 한 번에 뱃지 정보와 상세 정보 조회
            List<Map<String, Object>> badgeDetails = badgeDAO.getUserBadgesWithDetails(userId);
            
            // 이미지 경로 추가
            for (Map<String, Object> badge : badgeDetails) {
                String badgeId = (String) badge.get("BADGEID");
                badge.put("badgeImage", "/badge/image/" + badgeId);
                
                // 날짜 정보 추가 처리
                Object awardedDt = badge.get("AWARDEDDT");
                if (awardedDt != null) {
                    badge.put("awardedDate", awardedDt);
                }
                
                System.out.println("뱃지 정보: " + badgeId + " - " + badge.get("BADGENAME"));
            }
            
            System.out.println("JOIN으로 조회된 뱃지 수: " + badgeDetails.size());
            return badgeDetails;
        } catch (Exception e) {
            System.err.println("뱃지 상세 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // 뱃지 이름 매핑
    private String getBadgeName(String badgeId) {
        switch (badgeId) {
            case "water_badge01": return "물 절약 초심자";
            case "energy_badge01": return "에너지 절약 초심자";
            case "waste_badge01": return "자원 절약 초심자";
            default: return "알 수 없는 뱃지";
        }
    }
    
    // 뱃지 이미지 매핑 (더 이상 사용하지 않음, 호환성을 위해 유지)
    private String getBadgeImage(String badgeId) {
        // 이제 DB에서 직접 이미지를 로드하므로 이 메서드는 사용하지 않음
        return "/badge/image/" + badgeId; // ✅ 올바른 경로
    }
    
    // 오늘 총 완료 챌린지 수 (달력 표시용)
    public int getTodayTotalCompletedCount(String userId) {
        try {
            Date today = Date.valueOf(LocalDate.now());
            int count = challengeDAO.getTodayTotalCompletedCount(userId, today);
            System.out.println("오늘 총 완료 챌린지 수: " + count);
            return count;
        } catch (Exception e) {
            System.err.println("오늘 완료 챌린지 수 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    // 월별 완료 날짜 조회
    public List<Integer> getMonthlyCompletedDates(String userId, int year, int month) {
        try {
            return challengeDAO.getMonthlyCompletedDates(userId, year, month);
        } catch (Exception e) {
            System.err.println("월별 완료 날짜 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 포인트 내역 조회
    public List<Map<String, Object>> getPointHistory(String userId) {
        try {
            return challengeDAO.getPointHistory(userId);
        } catch (Exception e) {
            System.err.println("포인트 내역 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 카테고리별 진행상황 조회
    public Map<String, Integer> getCategoryProgress(String userId) {
        try {
            Map<String, Integer> progress = new HashMap<>();
            progress.put("물", challengeDAO.getUniqueCategoryCompletionCount(userId, "물"));
            progress.put("전기", challengeDAO.getUniqueCategoryCompletionCount(userId, "전기"));
            progress.put("자원", challengeDAO.getUniqueCategoryCompletionCount(userId, "자원"));
            return progress;
        } catch (Exception e) {
            System.err.println("카테고리 진행상황 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    
    // 오늘 획득한 뱃지 확인
    public boolean checkTodayNewBadges(String userId) {
        try {
            Date today = Date.valueOf(LocalDate.now());
            return badgeDAO.hasTodayBadge(userId, today);
        } catch (Exception e) {
            System.err.println("오늘 새 뱃지 확인 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 월별 8개 이상 완료한 날짜 조회
    public List<Integer> getMonthlyEightCompletedDates(String userId, int year, int month) {
        try {
            List<Map<String, Object>> dailyCompletions = challengeDAO.getMonthlyDailyCompletions(userId, year, month);
            List<Integer> eightCompleteDates = new ArrayList<>();
            
            for (Map<String, Object> daily : dailyCompletions) {
                Integer day = (Integer) daily.get("day");
                Integer count = (Integer) daily.get("count");
                
                if (count != null && count >= 8) {
                    eightCompleteDates.add(day);
                }
            }
            
            System.out.println("월별 8개 완료 날짜 조회 결과: " + eightCompleteDates);
            return eightCompleteDates;
        } catch (Exception e) {
            System.err.println("월별 8개 완료 날짜 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 오늘 새로 획득한 뱃지 상세 조회 (새로 추가)
    public List<Map<String, Object>> getTodayNewBadgesWithDetails(String userId) {
        try {
            Date today = Date.valueOf(LocalDate.now());
            System.out.println("오늘 새 뱃지 상세 조회 - 사용자: " + userId);
            
            // 오늘 획득한 뱃지가 있는지 먼저 확인
            if (!badgeDAO.hasTodayBadge(userId, today)) {
                return new ArrayList<>();
            }
            
            // 오늘 획득한 뱃지 목록 조회
            List<UserBadgeVO> todayBadges = badgeDAO.getUserBadges(userId);
            List<Map<String, Object>> newBadges = new ArrayList<>();
            
            // 오늘 날짜 필터링
            String todayString = today.toString();
            for (UserBadgeVO badge : todayBadges) {
                if (badge.getAwardedDt() != null && 
                    badge.getAwardedDt().toString().startsWith(todayString)) {
                    
                    Map<String, Object> badgeInfo = new HashMap<>();
                    badgeInfo.put("badgeId", badge.getBadgeId());
                    badgeInfo.put("badgeName", getBadgeName(badge.getBadgeId()));
                    badgeInfo.put("badgeImage", "/badge/image/" + badge.getBadgeId());
                    badgeInfo.put("awardedDate", badge.getAwardedDt());
                    newBadges.add(badgeInfo);
                }
            }
            
            System.out.println("오늘 새로 획득한 뱃지: " + newBadges.size() + "개");
            return newBadges;
        } catch (Exception e) {
            System.err.println("오늘 새 뱃지 상세 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // 절약 랭킹 조회 (토탈 포인트 높은 순으로 상위 4명)
    public List<Map<String, Object>> getTopRanking() {
        try {
            System.out.println("절약 랭킹 조회 시작");
            List<Map<String, Object>> ranking = challengeDAO.getTopRanking();
            System.out.println("조회된 랭킹 데이터: " + ranking.size() + "명");
            return ranking;
        } catch (Exception e) {
            System.err.println("랭킹 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    }