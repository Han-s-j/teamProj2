package com.project.apt.guide.vo;

public class PastWeatherAirVO {
	// 날짜 (yyyyMMdd)
	private String date;

	// 관측 시간 (필요 시)
	private String obsTime;

	// 동 이름
	private String dongName;

	// 기온
	private Double tempC;

	// 습도
	private Integer humidityPct;

	// 풍속
	private Double windSpeedMps;

	// 풍향
	private Integer windDirDeg;

	// 미세먼지(PM10)
	private String pm10;

	// 초미세먼지(PM2.5)
	private String pm25;

	// 오존
	private String o3;

	// getters & setters
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getObsTime() {
		return obsTime;
	}

	public void setObsTime(String obsTime) {
		this.obsTime = obsTime;
	}

	public String getDongName() {
		return dongName;
	}

	public void setDongName(String dongName) {
		this.dongName = dongName;
	}

	public Double getTempC() {
		return tempC;
	}

	public void setTempC(Double tempC) {
		this.tempC = tempC;
	}

	public Integer getHumidityPct() {
		return humidityPct;
	}

	public void setHumidityPct(Integer humidityPct) {
		this.humidityPct = humidityPct;
	}

	public Double getWindSpeedMps() {
		return windSpeedMps;
	}

	public void setWindSpeedMps(Double windSpeedMps) {
		this.windSpeedMps = windSpeedMps;
	}

	public Integer getWindDirDeg() {
		return windDirDeg;
	}

	public void setWindDirDeg(Integer windDirDeg) {
		this.windDirDeg = windDirDeg;
	}

	public String getPm10() {
		return pm10;
	}

	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public String getO3() {
		return o3;
	}

	public void setO3(String o3) {
		this.o3 = o3;
	}

	@Override
	public String toString() {
		return "PastWeatherAirVO [date=" + date + ", obsTime=" + obsTime + ", dongName=" + dongName + ", tempC=" + tempC
				+ ", humidityPct=" + humidityPct + ", windSpeedMps=" + windSpeedMps + ", windDirDeg=" + windDirDeg
				+ ", pm10=" + pm10 + ", pm25=" + pm25 + ", o3=" + o3 + "]";
	}
}