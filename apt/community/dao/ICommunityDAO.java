package com.project.apt.community.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.project.apt.community.vo.CommunityVO;
import com.project.apt.community.vo.ReplyVO;

@Mapper
public interface ICommunityDAO {
	
	// 게시물 등록
	int insertPost(CommunityVO board); 
	
	//모든 게시물 목록 조회
    List<CommunityVO> selectAllPosts();
    
    // 특정 게시물 상세 조회
    CommunityVO selectPostByBoardNo(int boardNo); // boardNo를 인자로 받아 CommunityVO 반환
    
    // 내 게시글 조회
    List<CommunityVO> selectMyPosts(String userId);
    
    // 게시글 논리 삭제
    int deletePost(int boardNo);
    
    // 댓글 등록
    int insertReply(ReplyVO reply);
    
    //특정 게시글의 댓글 목록 조회
    List<ReplyVO> getRepliesByBoardNo(int boardNo);
    
    // 스크롤 페이지
    //List<CommunityVO> selectPostsByPage(Map<String, Integer> paramMap);
    
    // 기존 메소드에 Map<String, Object>로 파라미터 타입 변경 및 orderBy 필드 추가
    // offset, endRow, orderBy(정렬 기준) 등을 Map에 담아서 전달
    List<CommunityVO> selectPostsByPage(Map<String, Object> paramMap);
    
    // 게시글 수정
    int updatePost(CommunityVO board);
    
    // 신고 기능 추가
    int updateBoardReportStatus(int boardNo);
    
    // ============ 관리자 기능 메서드들 추가 ============
    
    /**
     * 신고된 게시글 목록 조회 (관리자용)
     */
    List<CommunityVO> getReportedPosts();
    
    /**
     * 관리자 게시글 삭제 (IS_DELETED를 'Y'로 변경)
     */
    int adminDeletePost(int boardNo);
    
    /**
     * 신고 해제 (IS_REPORTED를 'N'으로 변경)
     */
    int unreportPost(int boardNo);
}