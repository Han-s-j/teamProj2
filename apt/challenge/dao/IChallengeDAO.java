package com.project.apt.challenge.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import java.sql.Date;

@Mapper
public interface IChallengeDAO {
    
    // 기존 메서드들
    void savePointHistory(@Param("userId") String userId, 
                         @Param("pointHistory") int pointHistory, 
                         @Param("reason") String reason, 
                         @Param("awardedDate") java.util.Date awardedDate);
    
    void updateTotalPoint(@Param("userId") String userId, 
                         @Param("pointHistory") int pointHistory);
    
    int getUserPoints(@Param("userId") String userId);
    
    int checkChallengeCompletedToday(@Param("userId") String userId,
                                     @Param("challengeName") String challengeName,
                                     @Param("today") Date today);

    List<String> getTodayCompletedChallenges(@Param("userId") String userId,
                                             @Param("today") Date today);

    
    // 카테고리별 완료 횟수 조회 (중복 포함)
    int getCategoryCompletionCount(@Param("userId") String userId, 
                                   @Param("category") String category);
    
    // 카테고리별 고유 챌린지 완료 개수 조회 (중복 제거) - 누락된 메서드
    int getUniqueCategoryCompletionCount(@Param("userId") String userId, 
                                        @Param("category") String category);
    
    
    // 오늘 총 완료 챌린지 수
    int getTodayTotalCompletedCount(@Param("userId") String userId,
                                   @Param("today") Date today);
    // 월별 완료 날짜 조회
    List<Integer> getMonthlyCompletedDates(@Param("userId") String userId, 
                                          @Param("year") int year, 
                                          @Param("month") int month);
    // 포인트 내역 조회
    List<Map<String, Object>> getPointHistory(@Param("userId") String userId);
    
    // 새로 추가할 메서드
    List<Map<String, Object>> getMonthlyDailyCompletions(@Param("userId") String userId, 
                                                          @Param("year") int year, 
                                                          @Param("month") int month);

    // 절약 랭킹 조회 (토탈 포인트 높은 순으로 상위 4명)
    List<Map<String, Object>> getTopRanking();
}