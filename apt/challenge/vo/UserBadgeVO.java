package com.project.apt.challenge.vo;

import java.util.Date;

public class UserBadgeVO {
    private String userId;
    private String badgeId;
    private Date awardedDt;
    
    public UserBadgeVO() {}
    
    // getter, setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getBadgeId() { return badgeId; }
    public void setBadgeId(String badgeId) { this.badgeId = badgeId; }
    
    public Date getAwardedDt() { return awardedDt; }
    public void setAwardedDt(Date awardedDt) { this.awardedDt = awardedDt; }
}