package com.project.apt.challenge.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.apt.challenge.vo.BadgeInfoVO;
import com.project.apt.challenge.vo.UserBadgeVO;
import java.util.List;
import java.util.Map;
import java.sql.Date;

@Mapper
public interface BadgeDAO {
    
    // 뱃지 정의 조회 (BADGE_INFO 테이블)
    List<BadgeInfoVO> getBadgeDefinitions();
    
    // 사용자 뱃지 조회 (USER_BADGE 테이블) - 기본 버전
    List<UserBadgeVO> getUserBadges(@Param("userId") String userId);
    
    // 사용자 뱃지 상세 정보
    List<Map<String, Object>> getUserBadgesWithDetails(@Param("userId") String userId);
    
    // 뱃지 획득 여부 확인
    boolean hasBadge(@Param("userId") String userId, @Param("badgeId") String badgeId);
    
    // 뱃지 획득 처리
    void awardBadge(@Param("userId") String userId, @Param("badgeId") String badgeId);
    
    // 뱃지 정보 조회
    BadgeInfoVO getBadgeInfo(@Param("badgeId") String badgeId);
    
    // 챌린지 성공 횟수 조회 (USER_CHALLENGE 테이블)
    Integer getSuccessCount(@Param("userId") String userId, @Param("chId") String chId);
    
    // 챌린지 성공 횟수 추가
    void insertSuccessCount(@Param("userId") String userId, @Param("chId") String chId, @Param("count") int count);
    
    // 챌린지 성공 횟수 업데이트
    void updateSuccessCount(@Param("userId") String userId, @Param("chId") String chId, @Param("count") int count);
    
    // 뱃지 진행도 조회 (JOIN 버전) - 새로 추가
    List<Map<String, Object>> getBadgeProgress(@Param("userId") String userId);
    
    // 카테고리별 뱃지 진행도 조회 (개선된 버전) - 새로 추가
    List<Map<String, Object>> getCategoryBadgeProgress(@Param("userId") String userId);
    
    // 오늘 획득한 뱃지 확인
    boolean hasTodayBadge(@Param("userId") String userId, @Param("today") Date today);
    
    // 오늘 획득한 뱃지 목록 조회 (상세 정보 포함) - 새로 추가
    List<Map<String, Object>> getTodayNewBadges(@Param("userId") String userId, @Param("today") Date today);
}