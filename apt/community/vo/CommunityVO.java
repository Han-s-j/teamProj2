package com.project.apt.community.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CommunityVO {
	private int boardNo; // BOARD_NO (NUMBER)
    private String userId; // USER_ID (VARCHAR2) - 로그인 사용자 ID
    private String boardTitle; // BOARD_TITLE (VARCHAR2)
    private String boardContent; // BOARD_CONTENT (CLOB)
    private Date publishedDate; // PUBLISHED_DATE (DATE) - Date 타입 유지
    private Date editedDate; // EDITED_DATE (DATE) - Date 타입 유지
    private String isDeleted; // IS_DELETED (VARCHAR2)
    private String isReported; // IS_REPORTED (VARCHAR2) - 신고 상태 필드 추가
    
    // 추가: 게시물 목록 화면에 표시할 닉네임과 아파트 이름
    private String nickname; // user_info 테이블의 NICKNAME 컬럼
    private String apartmentName; // apartments_address 테이블의 KAPT_NAME 컬럼
    
    // JavaScript에서 사용할 수 있도록 포맷된 날짜 문자열 추가
    private String formattedPublishedDate; // 포맷된 작성일
    private String formattedEditedDate; // 포맷된 수정일
    private int commentCount; // 댓글 수를 저장할 필드
}