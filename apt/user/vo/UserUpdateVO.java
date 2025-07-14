package com.project.apt.user.vo;

import lombok.Data;

@Data
public class UserUpdateVO {
	private String  userId; // 로그인된 사용자 ID
    private String userBlock; // 동 정보
    private Integer userFloor; // 층 정보
    private String kaptCode;  // 아파트 코드
}
