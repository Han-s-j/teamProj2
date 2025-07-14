package com.project.apt.community.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.apt.community.dao.ICommunityDAO;
import com.project.apt.community.dao.IReplyDAO;
import com.project.apt.community.vo.CommunityVO;
import com.project.apt.community.vo.ReplyVO;

@Service
public class CommunityService {
    
    @Autowired
    private ICommunityDAO communityDAO; // DAO 의존성 주입
    @Autowired
    private IReplyDAO replyDAO;
    
    // 내 게시글 조회
    public List<CommunityVO> getMyPosts(String userId) {
        return communityDAO.selectMyPosts(userId);
    }
    
    // 게시글 번호로 조회
    public CommunityVO getPostById(int boardNo) {
        return communityDAO.selectPostByBoardNo(boardNo);
    }
    
    // 게시글 논리 삭제
    public int deletePost(int boardNo) {
        return communityDAO.deletePost(boardNo);
    }
    // 댓글
    public List<ReplyVO> getRepliesByBoardNo(int boardNo) {
        return replyDAO.selectRepliesByBoardNo(boardNo); // ✔ 추천 방식
    }
    // 게시물 스크롤
    public List<CommunityVO> selectPostsByPage(Map<String, Object> paramMap) {
        return communityDAO.selectPostsByPage(paramMap);
    }
    // 댓글 수정
    public int updateReply(ReplyVO reply) {
        return replyDAO.updateReply(reply);
    }
    // 게시글 수정
    public int updatePost(CommunityVO board) {
    	return communityDAO.updatePost(board);
    }
    // 신고 기능 추가
    public boolean reportBoard(int boardNo) {
        try {
            // IS_REPORTED를 'Y'로 업데이트
            int result = communityDAO.updateBoardReportStatus(boardNo);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ============ 관리자 기능 메서드들 추가 ============
    
    /**
     * 신고된 게시글 목록 조회 (관리자용)
     */
    public List<CommunityVO> getReportedPosts() {
        try {
            return communityDAO.getReportedPosts();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 관리자 게시글 삭제 (IS_DELETED를 'Y'로 변경)
     */
    public boolean adminDeletePost(int boardNo) {
        try {
            System.out.println("CommunityService: 게시글 삭제 시도 - boardNo: " + boardNo);
            
            int result = communityDAO.adminDeletePost(boardNo);
            
            System.out.println("CommunityService: 삭제 쿼리 실행 결과 - affected rows: " + result);
            
            boolean success = result > 0;
            System.out.println("CommunityService: 삭제 성공 여부: " + success);
            
            return success;
        } catch (Exception e) {
            System.err.println("CommunityService: 게시글 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 신고 해제 (IS_REPORTED를 'N'으로 변경)
     */
    public boolean unreportPost(int boardNo) {
        try {
            System.out.println("CommunityService: 신고해제 시도 - boardNo: " + boardNo);
            
            int result = communityDAO.unreportPost(boardNo);
            
            System.out.println("CommunityService: 신고해제 쿼리 실행 결과 - affected rows: " + result);
            
            boolean success = result > 0;
            System.out.println("CommunityService: 신고해제 성공 여부: " + success);
            
            return success;
        } catch (Exception e) {
            System.err.println("CommunityService: 신고해제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}