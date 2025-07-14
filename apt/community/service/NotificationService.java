package com.project.apt.community.service;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;

import java.security.Security;
import java.util.*;

@Service
public class NotificationService {

	@Autowired
	private NotificationMapper notificationMapper;
	

	public NotificationService() {
		if (Security.getProvider("BC") == null) {
			// web push protocol 메시지를 암호화 복호화
			Security.addProvider(new BouncyCastleProvider());
		}
	}


	/**
	 * 1. PWA 구독 정보 저장 (기존 구독 삭제 후 새로 저장)
	 */
	@Transactional
	public boolean saveSubscription(String userId, Map<String, Object> subscription) {
		try {
			// 기존 구독 삭제
			notificationMapper.deleteSubscription(userId);

			// 새 구독 정보 파싱
			String endpoint = (String) subscription.get("endpoint");
			@SuppressWarnings("unchecked")
			Map<String, String> keys = (Map<String, String>) subscription.get("keys");
			String p256dhKey = keys.get("p256dh");
			String authKey = keys.get("auth");

			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			params.put("endpoint", endpoint);
			params.put("p256dhKey", p256dhKey);
			params.put("authKey", authKey);

			int result = notificationMapper.insertSubscription(params);

			// 구독 설정 업데이트
			upsertNotificationSetting(userId, "COMMUNITY", "REAL");

			return result > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 2. NOTIFICATIONS_SETTING UPSERT
	public void upsertNotificationSetting(String userId, String notiType, String guideTime) {
		Map<String, Object> param = Map.of("userId", userId, "notiType", notiType);
		int count = notificationMapper.countNotificationSetting(param);

		Map<String, Object> newParam = new HashMap<>(param);
		newParam.put("guideTime", guideTime);

		if (count > 0) {
			notificationMapper.updateNotificationSetting(newParam);
		} else {
			notificationMapper.insertNotificationSetting(newParam);
		}
	}

	// 3. 구독 상태 조회
	public String getNotificationStatus(String userId, String notiType) {
		Map<String, Object> param = Map.of("userId", userId, "notiType", notiType);
		return notificationMapper.selectNotificationStatus(param);
	}

	// 4. 구독 취소
	public void unsubscribe(String userId) {
		Map<String, Object> param = Map.of("userId", userId, "notiType", "COMMUNITY");
		notificationMapper.updateNotificationSettingUnsubscribe(param);
	}

	// 5. 사용자 포인트 조회
	public Integer getUserPoints(String userId) {
		return notificationMapper.selectUserPoints(userId);
	}

	// 6. 포인트 차감
	public void deductPoints(String userId, int pointCost) {
		Map<String, Object> param = Map.of("userId", userId, "pointCost", pointCost);
		notificationMapper.deductUserPoints(param);
	}

	// 7. 포인트 로그 저장
	public void insertPointLog(String userId, int pointChange, String reason) {
		Map<String, Object> param = Map.of("userId", userId, "pointChange", pointChange, "reason", reason);
		notificationMapper.insertPointLog(param);
	}

	// 8. 최근 알림 목록
	public List<Map<String, Object>> getRecentNotifications(String userId) {
		return notificationMapper.selectRecentNotifications(userId);
	}

	// 9. polling용
	public List<Map<String, Object>> getNotificationsSince(String userId, long lastCheck) {
		Map<String, Object> param = Map.of("userId", userId, "lastCheck", lastCheck);
		return notificationMapper.selectNotificationsSince(param);
	}

	// 10. 특정 동 구독자
	public List<Map<String, Object>> getSubscriptionsByBuildings(String kaptCode, List<String> buildings) {
		Map<String, Object> param = new HashMap<>();
		param.put("kaptCode", kaptCode);
		param.put("buildings", buildings);
		return notificationMapper.selectSubscriptionsByBuildings(param);
	}

	// 11. 전체 구독자
	public List<Map<String, Object>> getAllSubscribedUsers() {
		return notificationMapper.selectAllSubscribedUsers();
	}

	// 12. 알림 이력 저장
	public void insertNotificationHistory(String userId, String senderId, String targetScope, String message) {
		Map<String, Object> param = Map.of("userId", userId, "senderId", senderId, "targetScope", targetScope,
				"message", message);
		notificationMapper.insertNotificationHistory(param);
	}

	// 13. 아파트 이름 조회
	public String getApartmentName(String kaptCode) {
		return notificationMapper.selectApartmentName(kaptCode);
	}

	// 14. apartments_block 동 목록
	public List<Map<String, Object>> getBuildingsByKaptCode(String kaptCode) {
		return notificationMapper.selectBuildingsByKaptCode(kaptCode);
	}

	// 15. USER_INFO 동 목록
	public List<Map<String, Object>> getBuildingsFromUserInfo(String kaptCode) {
		return notificationMapper.selectBuildingsFromUserInfo(kaptCode);
	}

	// ✅ 커뮤니티 알림 전송 및 포인트 차감
	@Transactional
	public Map<String, Object> sendCommunityNotification(String userId, Map<String, Object> requestData) {
		PushService pushService = new PushService();
	    String message = (String) requestData.get("message");
	    if (message == null || message.trim().isEmpty())
	        throw new IllegalArgumentException("메시지는 필수입니다.");

	    Integer pointCost = (Integer) requestData.getOrDefault("pointCost", 300);
	    Integer userPoints = notificationMapper.selectUserPoints(userId);
	    if (userPoints == null || userPoints < pointCost)
	        throw new IllegalArgumentException("포인트 부족");

	    // 포인트 차감
	    notificationMapper.deductUserPoints(Map.of("userId", userId, "pointCost", pointCost));

	    // 범위 해석
	    String targetScope = (String) requestData.getOrDefault("targetScope", "entire_apt");
	    List<Map<String, Object>> targetUsers;

	    if (targetScope.startsWith("specific_buildings")) {
	        String[] parts = targetScope.split(":");
	        String kaptCode = parts[1];
	        List<String> buildings = Arrays.asList(parts[2].split(","));
	        Map<String, Object> param = Map.of("kaptCode", kaptCode, "buildings", buildings);
	        targetUsers = notificationMapper.selectSubscriptionsByBuildings(param);
	    } else {
	        targetUsers = notificationMapper.selectAllSubscribedUsers();
	    }

	    List<String> targetUserIds = targetUsers.stream()
	            .map(user -> (String) user.get("USER_ID"))
	            .toList();

	    List<Map<String, Object>> subscriptions = notificationMapper.selectActiveSubscriptions(Map.of("userIds", targetUserIds));

	    int successCount = 0;
	    for (Map<String, Object> sub : subscriptions) {
	        try {
	            Subscription subscription = new Subscription(
	                    (String) sub.get("ENDPOINT"),
	                    new Subscription.Keys((String) sub.get("P256DHKEY"), (String) sub.get("AUTHKEY"))
	            );
	            String payload = String.format("{\"title\":\"새 알림\",\"body\":\"%s\"}", message);
	            pushService.send(new Notification(subscription, payload));
	            successCount++;
	        } catch (Exception e) {
	            System.out.println("❌ 푸시 전송 실패: " + e.getMessage());
	        }
	    }

	    // 이력 저장
	    for (String targetId : targetUserIds) {
	        notificationMapper.insertNotificationHistory(Map.of(
	                "userId", targetId,
	                "senderId", userId,
	                "targetScope", targetScope,
	                "message", message
	        ));
	    }

	    return Map.of(
	            "success", true,
	            "recipientCount", successCount,
	            "pointsUsed", pointCost,
	            "remainingPoints", userPoints - pointCost
	    );
	}
}