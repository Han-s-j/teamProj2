package com.project.apt.guide.vo;

public class GuideVO {

    private String kaptCode;
    private String kaptName;
    private String aptBlock;
    private Double latitude;
    private Double longitude;
    private String direction;
    private String newAddress;
    private Integer floor;
    private String dong;
    private String nickname;
    private String stationName; // 미세먼지 API 측정소명 추가
    private String sidoName;    // 🔴 시도명 필드 추가!

    public GuideVO() {
    }

    public String getKaptCode() {
        return kaptCode;
    }

    public void setKaptCode(String kaptCode) {
        this.kaptCode = kaptCode;
    }

    public String getKaptName() {
        return kaptName;
    }

    public void setKaptName(String kaptName) {
        this.kaptName = kaptName;
    }

    public String getAptBlock() {
        return aptBlock;
    }

    public void setAptBlock(String aptBlock) {
        this.aptBlock = aptBlock;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    // 🔴 sidoName 필드의 getter 추가 (기존 TODO 메서드 수정)
    public String getSidoName() {
        return sidoName;
    }

    // 🔴 sidoName 필드의 setter 추가
    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }

    @Override
    public String toString() {
        return "GuideVO [kaptCode=" + kaptCode + ", kaptName=" + kaptName + ", aptBlock=" + aptBlock + ", latitude="
                + latitude + ", longitude=" + longitude + ", direction=" + direction + ", newAddress=" + newAddress
                + ", floor=" + floor + ", dong=" + dong + ", nickname=" + nickname + ", stationName=" + stationName
                + ", sidoName=" + sidoName + "]"; // 🔴 toString에도 sidoName 추가
    }
}