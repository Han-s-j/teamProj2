package com.project.apt.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.apt.common.dao.ICommonDAO;
import com.project.apt.user.dao.IUserDAO;
import com.project.apt.user.vo.UserAdminVO;

@Service
public class CommonService {

    @Autowired
    private IUserDAO userDAO;
    
    @Autowired
    private ICommonDAO dao;
    
    // 기존 메서드들
    public List<UserAdminVO> getAllUsers(){
    	return userDAO.getAllUsers();
    }
    
    public int getAllUserCount() {
    	return dao.getAllUserCount();
    }
    
    public int getUserPoint() {
    	return dao.getUserPoint();
    }
    
    public int getAllBoardCount() {
    	return dao.getAllBoardCount();
    }
    
    // 새로 추가할 차트용 메서드들
    public int getTodayVisitors() {
        return dao.getTodayVisitors();
    }
    
    public int getTodaySignups() {
        return dao.getTodaySignups();
    }
    
    public int getTodayWithdrawals() {
        return dao.getTodayWithdrawals();
    }
    
    public List<Map<String, Object>> getVisitorsChartData() {
        return dao.getVisitorsChartData();
    }
    
    public List<Map<String, Object>> getUserChangeChartData() {
        return dao.getUserChangeChartData();
    }
}