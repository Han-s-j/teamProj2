package com.project.apt.guide.vo;

public class DailySummaryVO {
    private String date;
    private String formattedDate;
    private double avgTemperature;
    private double avgHumidity;
    private String recommendTimes; // 예: "12~14시, 16시"
    private String precipitationTimes; // 예: "09~10시 눈/비 예보"
    private String ventStatus; // 환기 추천 상태 (추천/비추천)
    private String dustGrade; // 미세먼지 등급
    private String dateDisplay; // 요일 추가 날짜
    private double avgWindSpeed; // 평균 풍속
    private String dryingGrade; // 건조 적합도
    
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFormattedDate() {
		return formattedDate;
	}
	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}
	public double getAvgTemperature() {
		return avgTemperature;
	}
	public void setAvgTemperature(double avgTemperature) {
		this.avgTemperature = avgTemperature;
	}
	public double getAvgHumidity() {
		return avgHumidity;
	}
	public void setAvgHumidity(double avgHumidity) {
		this.avgHumidity = avgHumidity;
	}
	public String getRecommendTimes() {
		return recommendTimes;
	}
	public void setRecommendTimes(String recommendTimes) {
		this.recommendTimes = recommendTimes;
	}
	public String getPrecipitationTimes() {
		return precipitationTimes;
	}
	public void setPrecipitationTimes(String precipitationTimes) {
		this.precipitationTimes = precipitationTimes;
	}
	public String getVentStatus() {
		return ventStatus;
	}
	public void setVentStatus(String ventStatus) {
		this.ventStatus = ventStatus;
	}
	public String getDustGrade() {
		return dustGrade;
	}
	public void setDustGrade(String dustGrade) {
		this.dustGrade = dustGrade;
	}
	public String getDateDisplay() {
		return dateDisplay;
	}
	public void setDateDisplay(String dateDisplay) {
		this.dateDisplay = dateDisplay;
	}
	public double getAvgWindSpeed() {
		return avgWindSpeed;
	}
	public void setAvgWindSpeed(double avgWindSpeed) {
		this.avgWindSpeed = avgWindSpeed;
	}
	public String getDryingGrade() {
		return dryingGrade;
	}
	public void setDryingGrade(String dryingGrade) {
		this.dryingGrade = dryingGrade;
	}
	@Override
	public String toString() {
		return "DailySummaryVO [date=" + date + ", formattedDate=" + formattedDate + ", avgTemperature="
				+ avgTemperature + ", avgHumidity=" + avgHumidity + ", recommendTimes=" + recommendTimes
				+ ", precipitationTimes=" + precipitationTimes + ", ventStatus=" + ventStatus + ", dustGrade="
				+ dustGrade + ", dateDisplay=" + dateDisplay + ", avgWindSpeed=" + avgWindSpeed + ", dryingGrade="
				+ dryingGrade + "]";
	}
    
	
}