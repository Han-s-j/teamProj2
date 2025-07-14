package com.project.apt.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.apt.common.dao.IVisitorDAO;

@Service
public class VisitorService {
    
    @Autowired
    private IVisitorDAO visitorDAO;
    
    public void recordVisit(String userId, String ipAddress) {
        try {
            visitorDAO.insertVisit(userId, ipAddress);
        } catch (Exception e) {
            // 중복 방문이거나 오류시 무시
        }
    }
}