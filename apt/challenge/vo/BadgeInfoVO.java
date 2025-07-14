package com.project.apt.challenge.vo;

import java.sql.Blob;

public class BadgeInfoVO {
    private String badgeId;
    private String chId;
    private String badgeName;
    private String condition;
    private byte[] badgeImg;
    public BadgeInfoVO() {}
    
    public BadgeInfoVO(String badgeId, String chId, String badgeName, String condition) {
        this.badgeId = badgeId;
        this.chId = chId;
        this.badgeName = badgeName;
        this.condition = condition;
    }
    
    // getter, setter
    public String getBadgeId() { return badgeId; }
    public void setBadgeId(String badgeId) { this.badgeId = badgeId; }
    
    public String getChId() { return chId; }
    public void setChId(String chId) { this.chId = chId; }
    
    public String getBadgeName() { return badgeName; }
    public void setBadgeName(String badgeName) { this.badgeName = badgeName; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

	public byte[] getBadgeImg() {
		return badgeImg;
	}

	public void setBadgeImg(byte[] badgeImg) {
		this.badgeImg = badgeImg;
	}
    
}
