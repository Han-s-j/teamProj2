package com.project.apt.challenge.vo;

public class ChallengeVO {

	    private String userId;     // 사용자 아이디
	    private int pointHistory;  // 포인트 내역
	    private String reason;     // 포인트 변동 이유
	    private String awardedDate; // 포인트 적립 일자

	    // Getters and Setters
	    public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }

	    public int getPointHistory() {
	        return pointHistory;
	    }

	    public void setPointHistory(int pointHistory) {
	        this.pointHistory = pointHistory;
	    }

	    public String getReason() {
	        return reason;
	    }

	    public void setReason(String reason) {
	        this.reason = reason;
	    }

	    public String getAwardedDate() {
	        return awardedDate;
	    }

	    public void setAwardedDate(String awardedDate) {
	        this.awardedDate = awardedDate;
	    }
	}


