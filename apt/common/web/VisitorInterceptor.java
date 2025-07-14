package com.project.apt.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.project.apt.common.service.VisitorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class VisitorInterceptor implements HandlerInterceptor {
    
    @Autowired
    private VisitorService visitorService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 메인 페이지 접속시에만 기록
            String requestURI = request.getRequestURI();
            if ("/".equals(requestURI) || "/home".equals(requestURI) || "/main".equals(requestURI)) {
                
                HttpSession session = request.getSession();
                String userId = (String) session.getAttribute("userId");
                String ipAddress = getClientIpAddress(request);
                
                // 방문 기록
                visitorService.recordVisit(userId, ipAddress);
            }
        } catch (Exception e) {
            // 방문자 추적 실패해도 페이지는 정상 동작
            e.printStackTrace();
        }
        
        return true;
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}