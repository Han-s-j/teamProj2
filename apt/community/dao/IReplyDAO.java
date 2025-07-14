package com.project.apt.community.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.project.apt.community.vo.ReplyVO;

@Mapper
public interface IReplyDAO {
    
    // 댓글 등록
    int insertReply(ReplyVO reply);
    
    // 특정 게시글의 댓글 목록 조회
    List<ReplyVO> selectRepliesByBoardNo(int boardNo);
    
    // 내가 작성한 댓글 목록 조회
    List<ReplyVO> selectMyReplies(String userId);
    
    // 댓글 논리 삭제
    int deleteReply(int replyNo);
    
    // 특정 댓글 조회
    ReplyVO selectReplyByNo(int replyNo);
    
    // 댓글 수정
    int updateReply(ReplyVO reply);
    
}