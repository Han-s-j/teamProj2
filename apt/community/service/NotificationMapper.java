package com.project.apt.community.service;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface NotificationMapper {

    // === 구독 정보 관리 ===
    
    // 1. 푸시 구독 저장
    public int insertSubscription(Map<String, Object> param);

    // 2. 구독 삭제
    public int deleteSubscription(String userId);

    // 3. NOTIFICATIONS_SETTING 개수 조회
    public int countNotificationSetting(Map<String, Object> param);

    // 4. NOTIFICATIONS_SETTING 업데이트
    public int updateNotificationSetting(Map<String, Object> param);

    // 5. NOTIFICATIONS_SETTING 삽입
    public int insertNotificationSetting(Map<String, Object> param);

    // 6. 구독 상태 조회
    public String selectNotificationStatus(Map<String, Object> param);

    // 7. 사용자 포인트 조회
    public Integer selectUserPoints(String userId);

    // 8. 포인트 차감
    public int deductUserPoints(Map<String, Object> param);

    // 9. 포인트 로그 저장
    public int insertPointLog(Map<String, Object> param);

    // 10. 최근 알림 리스트
    public List<Map<String, Object>> selectRecentNotifications(String userId);

    // 11. 특정 동 사용자 구독 조회
    public List<Map<String, Object>> selectSubscriptionsByBuildings(Map<String, Object> param);

    // 12. 전체 구독 사용자 조회
    public List<Map<String, Object>> selectAllSubscribedUsers();

    // 13. NOTIFICATIONS_HISTORY 저장
    public int insertNotificationHistory(Map<String, Object> param);

    // 14. polling용 최근 알림
    public List<Map<String, Object>> selectNotificationsSince(Map<String, Object> param);

    // 15. 아파트 이름 조회
    public String selectApartmentName(String kaptCode);

    // 16. apartments_block 기준 동목록
    public List<Map<String, Object>> selectBuildingsByKaptCode(String kaptCode);

    // 17. USER_INFO 기준 동목록
    public List<Map<String, Object>> selectBuildingsFromUserInfo(String kaptCode);
    
    // ✅ 구독 해제용 업데이트 쿼리용 메소드 (기존 없었음)
    public int updateNotificationSettingUnsubscribe(Map<String, Object> param);
    // ✅ 포인트 업데이트용
    public int updateUserPoints(Map<String, Object> param);
    // 특정 대상자 endpoint 정보 조회
    public List<Map<String, Object>> selectActiveSubscriptions(Map<String, Object> param);
}