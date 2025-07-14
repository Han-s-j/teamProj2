package com.project.apt.user.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserAdminVO {

	// 관리자 조회용 VO
    private String userId;       // 아이디
    private String nickname;     // 닉네임
    private String kaptCode;     // 아파트 코드
    private String userBlock;    // 동
    private int userFloor;       // 층수
    private int totalPoint;      // 누적 포인트
    private LocalDate signupDate; // 가입일 (java.time.LocalDate 추천)
}
