package com.project.apt.guide.vo;

public class WeeklyVentilationVO {
	private String date;
	private double tempAvg;
	private double humiAvg;
	private double windAvg;
	private String weatherGrade;
	private String weatherReason;
	private String dustReason;
	private String finalRecommend;
	private String dryingGrade;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getTempAvg() {
		return tempAvg;
	}

	public void setTempAvg(double tempAvg) {
		this.tempAvg = tempAvg;
	}

	public double getHumiAvg() {
		return humiAvg;
	}

	public void setHumiAvg(double humiAvg) {
		this.humiAvg = humiAvg;
	}

	public double getWindAvg() {
		return windAvg;
	}

	public void setWindAvg(double windAvg) {
		this.windAvg = windAvg;
	}

	public String getWeatherGrade() {
		return weatherGrade;
	}

	public void setWeatherGrade(String weatherGrade) {
		this.weatherGrade = weatherGrade;
	}

	public String getWeatherReason() {
		return weatherReason;
	}

	public void setWeatherReason(String weatherReason) {
		this.weatherReason = weatherReason;
	}

	public String getDustReason() {
		return dustReason;
	}

	public void setDustReason(String dustReason) {
		this.dustReason = dustReason;
	}

	public String getFinalRecommend() {
		return finalRecommend;
	}

	public void setFinalRecommend(String finalRecommend) {
		this.finalRecommend = finalRecommend;
	}

	public String getDryingGrade() {
		return dryingGrade;
	}

	public void setDryingGrade(String dryingGrade) {
		this.dryingGrade = dryingGrade;
	}

	@Override
	public String toString() {
		return "WeeklyVentilationVO [date=" + date + ", tempAvg=" + tempAvg + ", humiAvg=" + humiAvg + ", windAvg="
				+ windAvg + ", weatherGrade=" + weatherGrade + ", weatherReason=" + weatherReason + ", dustReason="
				+ dustReason + ", finalRecommend=" + finalRecommend + ", dryingGrade=" + dryingGrade + "]";
	}

}