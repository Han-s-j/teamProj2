package com.project.apt.community.vo;

import java.util.Date;
import lombok.Data;

@Data
public class ReplyVO {
    private String userId;        // USER_ID (VARCHAR2) - 작성자 ID
    private int boardNo;          // BOARD_NO (NUMBER) - 게시글 번호
    private int replyNo;          // REPLY_NO (NUMBER) - 댓글 번호 (PK)
    private Integer parentNo;     // PARENT_NO (NUMBER) - 부모 댓글 번호 (대댓글용)
    private String replyContent;  // REPLY_CONTENT (VARCHAR2) - 댓글 내용
    private Date publishedDate;   // PUBLISHED_DATE (DATE) - 작성일
    private String isDeleted;     // IS_DELETED (VARCHAR2) - 삭제 여부
    
    // 조인용 필드들
    private String nickname;      // user_info 테이블의 NICKNAME
    private String apartmentName; // apartments_address 테이블의 KAPT_NAME
    private String boardTitle;    // board 테이블의 BOARD_TITLE (어떤 게시글의 댓글인지 표시용)
    
    // 포맷된 날짜
    private String formattedPublishedDate; // 포맷된 작성일
    //private int replyLevel; // 댓글의 계층 레벨을 저장할 필드
}