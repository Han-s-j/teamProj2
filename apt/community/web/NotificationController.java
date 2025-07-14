package com.project.apt.community.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.apt.community.service.NotificationService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class NotificationController {

    @Autowired
    private NotificationService service;

    private String getUserIdFromSession(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            try {
                java.lang.reflect.Method getUserId = loginUser.getClass().getMethod("getUserId");
                return (String) getUserId.invoke(loginUser);
            } catch (Exception e) {
                String userString = loginUser.toString();
                if (userString.contains("userId=")) {
                    int start = userString.indexOf("userId=") + 7;
                    int end = userString.indexOf(",", start);
                    if (end == -1) end = userString.indexOf(")", start);
                    return userString.substring(start, end);
                }
            }
        }
        return null;
    }

    @GetMapping("/api/notifications/user/points")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserPoints(HttpSession session) {
        String userId = getUserIdFromSession(session);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("success", false, "message", "인증되지 않은 사용자입니다."));
        Integer points = service.getUserPoints(userId);
        return ResponseEntity.ok(Map.of("success", true, "userId", userId, "points", points != null ? points : 0));
    }

    @PostMapping("/api/notifications/subscribe")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> subscribeToPush(@RequestBody Map<String, Object> subscriptionData, HttpSession session) {
        String userId = getUserIdFromSession(session);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("success", false, "message", "인증되지 않은 사용자입니다."));
        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) subscriptionData.get("subscription");
        if (subscriptionData == null) throw new IllegalArgumentException("subscription 데이터가 누락되었습니다.");
        boolean result = service.saveSubscription(userId, payload);
        return result ? ResponseEntity.ok(Map.of("success", true, "message", "푸시 알림 구독 완료")) : ResponseEntity.status(500).body(Map.of("success", false, "message", "구독 실패"));
    }

    @PostMapping("/api/notifications/unsubscribe")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> unsubscribeFromPush(HttpSession session) {
        String userId = getUserIdFromSession(session);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("success", false, "message", "인증되지 않은 사용자입니다."));
        service.unsubscribe(userId);
        return ResponseEntity.ok(Map.of("success", true, "message", "구독이 취소되었습니다."));
    }

    @GetMapping("/api/notifications/subscription/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSubscriptionStatus(HttpSession session) {
        String userId = getUserIdFromSession(session);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("success", false, "message", "인증되지 않은 사용자입니다."));
        String status = service.getNotificationStatus(userId, "COMMUNITY");
        return ResponseEntity.ok(Map.of("success", true, "isSubscribed", "Y".equals(status)));
    }

    @GetMapping("/api/notifications/recent")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRecentNotifications(HttpSession session) {
        String userId = getUserIdFromSession(session);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("success", false, "message", "로그인이 필요합니다."));
        List<Map<String, Object>> notifications = service.getRecentNotifications(userId);
        return ResponseEntity.ok(Map.of("success", true, "notifications", notifications));
    }

    @GetMapping("/api/notifications/poll")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> pollNotifications(@RequestParam(defaultValue = "0") Long lastCheck, HttpSession session) {
        String userId = getUserIdFromSession(session);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("success", false));
        List<Map<String, Object>> notifications = service.getNotificationsSince(userId, lastCheck);
        return ResponseEntity.ok(Map.of("success", true, "hasNew", !notifications.isEmpty(), "notifications", notifications, "currentTime", System.currentTimeMillis()));
    }
    
    @PostMapping("/api/notifications/send")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendCommunityNotification(@RequestBody Map<String, Object> requestData, HttpSession session) {
        String userId = getUserIdFromSession(session);
        if (userId == null)
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "인증되지 않은 사용자입니다."));
        try {
            Map<String, Object> result = service.sendCommunityNotification(userId, requestData);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "알림 전송 실패", "error", e.getMessage()));
        }
    }
} 
