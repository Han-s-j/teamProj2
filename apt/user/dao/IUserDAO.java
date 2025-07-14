package com.project.apt.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.apt.user.vo.UserAdminVO;
import com.project.apt.user.vo.UserUpdateVO;
import com.project.apt.user.vo.UserVO;

@Mapper
public interface IUserDAO {

	// 회원가입 처리
	int saveUser(UserVO vo);

	// 계정 삭제 메서드 추가
	void deleteUser(String userId);

	// 아이디로 사용자 정보를 조회하는 메서드
	UserVO getUserById(String userId);

	// 관리소 전화번호로 아파트 코드 조회
	String findKaptCodeByManagementOfficeTel(@Param("managementOfficeTel") String managementOfficeTel);

	// 사용자 정보 업데이트
	void updateUserInfo(UserUpdateVO userUpdateVo);
	
	// apartments_address 테이블에서 kapt_code를 통해 kapt_name을 조회
	String findAptNameByKaptCode(@Param("kaptCode") String kaptCode);
	// IUserDAO.java에 추가
	Map<String, Object> getUserSubscriptionInfo(@Param("userId") String userId);
	// 사용자 전체 조회 : 관리자
	List<UserAdminVO> getAllUsers();

}