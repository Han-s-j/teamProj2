package com.project.apt.user.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.apt.user.dao.IUserDAO;
import com.project.apt.user.vo.UserUpdateVO;
import com.project.apt.user.vo.UserVO;

@Service
public class UserService {

    @Autowired
    private IUserDAO userDAO;
    

    // 계정 삭제 처리
    public void deleteUser(String userId) {
        userDAO.deleteUser(userId);  // DAO에서 삭제 처리
    }

    // 회원가입 처리
    public boolean register(UserVO vo) {
        System.out.println(vo);
        int result = userDAO.saveUser(vo);
        return result > 0;  // 저장 성공 여부 반환
    }

    // 로그인 처리
    public UserVO login(String userId, String userPw) {
        // 사용자 정보를 아이디로 조회
        UserVO user = userDAO.getUserById(userId);

        if (user == null) {
            return null; // 사용자 아이디가 없으면 null 반환
        }

        // BCryptPasswordEncoder를 사용하여 입력된 비밀번호와 저장된 비밀번호를 비교
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(userPw, user.getUserPw())) {
            return user; // 비밀번호가 일치하면 사용자 정보 반환
        } else {
            return null; // 비밀번호가 일치하지 않으면 null 반환
        } 
    }
	public String findKaptCodeByManagementOfficeTel(String managementOfficeTel) {
        return userDAO.findKaptCodeByManagementOfficeTel(managementOfficeTel);
    }
	
	public String findAptNameByKaptCode(String kaptCode) {
	    return userDAO.findAptNameByKaptCode(kaptCode);
	}
	
	// OCR 결과를 바탕으로 사용자 정보를 업데이트
	@Transactional // DB 업데이트는 트랜잭션으로 묶는 것이 좋습니다.
    public void updateUserInfo(UserUpdateVO userUpdateVo) {
        userDAO.updateUserInfo(userUpdateVo);
    }
	public UserVO getUserById(String userId) {
		return userDAO.getUserById(userId);
	}
	// UserService.java에 추가
	public Map<String, Object> getUserSubscriptionInfo(String userId) {
	    try {
	        return userDAO.getUserSubscriptionInfo(userId);
	    } catch (Exception e) {
	        System.err.println("구독 정보 조회 실패: " + e.getMessage());
	        return null;
	    }
	}
}
