package com.project.apt.guide.vo;

public class WeatherLogVO {
    private String date;        // 예: 20240704
    private String time;        // 예: 1200
    private String temperature; // String으로 받아서 parseDouble 처리
    private String humidity;
    private String windSpeed;
    private String windDirection;
    private String sky;
    private String precipitation;
    

	public WeatherLogVO(String date, String time, String temperature, String humidity, String windSpeed,
	                    String windDirection, String sky, String precipitation) {
	    this.date = date;
	    this.time = time;
	    this.temperature = temperature;
	    this.humidity = humidity;
	    this.windSpeed = windSpeed;
	    this.windDirection = windDirection;
	    this.sky = sky;
	    this.precipitation = precipitation;
	}
    
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
	public String getSky() {
		return sky;
	}
	public void setSky(String sky) {
		this.sky = sky;
	}
	public String getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}
	@Override
	public String toString() {
		return "WeatherLogVO [date=" + date + ", time=" + time + ", temperature=" + temperature + ", humidity="
				+ humidity + ", windSpeed=" + windSpeed + ", windDirection=" + windDirection + ", sky=" + sky
				+ ", precipitation=" + precipitation + "]";
	}
    
    
}