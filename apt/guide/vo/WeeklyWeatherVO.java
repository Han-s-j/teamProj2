package com.project.apt.guide.vo;

public class WeeklyWeatherVO {

	private String date; // "20250703"
	private String time; // "14:00"
	private String temperature;
	private String humidity;
	private String windSpeed;
	private String windDirection;
	private String precipitation;
	private String sky;
	private String ventGrade;
	private String ventReason;
	private String ventRecommend;
	private String dustGrade;
	private Boolean dustRecommend;
	private String recommendTime;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}

	public String getSky() {
		return sky;
	}

	public void setSky(String sky) {
		this.sky = sky;
	}

	public String getVentGrade() {
		return ventGrade;
	}

	public void setVentGrade(String ventGrade) {
		this.ventGrade = ventGrade;
	}

	public String getVentReason() {
		return ventReason;
	}

	public void setVentReason(String ventReason) {
		this.ventReason = ventReason;
	}

	public String getVentRecommend() {
		return ventRecommend;
	}

	public void setVentRecommend(String ventRecommend) {
		this.ventRecommend = ventRecommend;
	}

	public String getDustGrade() {
		return dustGrade;
	}

	public void setDustGrade(String dustGrade) {
		this.dustGrade = dustGrade;
	}

	public Boolean getDustRecommend() {
		return dustRecommend;
	}

	public void setDustRecommend(Boolean dustRecommend) {
		this.dustRecommend = dustRecommend;
	}

	public String getRecommendTime() {
		return recommendTime;
	}

	public void setRecommendTime(String recommendTime) {
		this.recommendTime = recommendTime;
	}

	@Override
	public String toString() {
		return "WeeklyWeatherVO [date=" + date + ", time=" + time + ", temperature=" + temperature + ", humidity="
				+ humidity + ", windSpeed=" + windSpeed + ", windDirection=" + windDirection + ", precipitation="
				+ precipitation + ", sky=" + sky + ", ventGrade=" + ventGrade + ", ventReason=" + ventReason
				+ ", ventRecommend=" + ventRecommend + ", dustGrade=" + dustGrade + ", dustRecommend=" + dustRecommend
				+ ", recommendTime=" + recommendTime + "]";
	}

}