package com.project.apt.guide.vo; // 패키지명 수정: service가 아닌 vo 패키지에 있어야 합니다.

public class AirQualityVO {
    private String stationName; // 측정소명
    private String dataTime;    // 측정일시
    private String sidoName;    // 시도명
    private String pm10Value;   // 미세먼지(PM10) 농도
    private String pm10Grade;   // 미세먼지(PM10) 등급
    private String pm25Value;   // 초미세먼지(PM2.5) 농도
    private String pm25Grade;   // 초미세먼지(PM2.5) 등급
    private String o3Value;     // 오존 농도
    private String o3Grade;     // 오존 등급
    private String coValue;     // 일산화탄소 농도
    private String coGrade;     // 일산화탄소 등급
    private String no2Value;    // 이산화질소 농도
    private String no2Grade;    // 이산화질소 등급
    private String so2Value;    // 아황산가스 농도
    private String so2Grade;    // 아황산가스 등급

    // Getters and Setters
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getSidoName() {
        return sidoName;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

    public String getPm10Grade() {
        return pm10Grade;
    }

    public void setPm10Grade(String pm10Grade) {
        this.pm10Grade = pm10Grade;
    }

    public String getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(String pm25Value) {
        this.pm25Value = pm25Value;
    }

    public String getPm25Grade() {
        return pm25Grade;
    }

    public void setPm25Grade(String pm25Grade) {
        this.pm25Grade = pm25Grade;
    }

    public String getO3Value() {
        return o3Value;
    }

    public void setO3Value(String o3Value) {
        this.o3Value = o3Value;
    }

    public String getO3Grade() {
        return o3Grade;
    }

    public void setO3Grade(String o3Grade) {
        this.o3Grade = o3Grade;
    }

    public String getCoValue() {
        return coValue;
    }

    public void setCoValue(String coValue) {
        this.coValue = coValue;
    }

    public String getCoGrade() {
        return coGrade;
    }

    public void setCoGrade(String coGrade) {
        this.coGrade = coGrade;
    }

    public String getNo2Value() {
        return no2Value;
    }

    public void setNo2Value(String no2Value) {
        this.no2Value = no2Value;
    }

    public String getNo2Grade() {
        return no2Grade;
    }

    public void setNo2Grade(String no2Grade) {
        this.no2Grade = no2Grade;
    }

    public String getSo2Value() {
        return so2Value;
    }

    public void setSo2Value(String so2Value) {
        this.so2Value = so2Value;
    }

    public String getSo2Grade() {
        return so2Grade;
    }

    public void setSo2Grade(String so2Grade) {
        this.so2Grade = so2Grade;
    }

	@Override
	public String toString() {
		return "AirQualityVO [stationName=" + stationName + ", dataTime=" + dataTime + ", sidoName=" + sidoName
				+ ", pm10Value=" + pm10Value + ", pm10Grade=" + pm10Grade + ", pm25Value=" + pm25Value + ", pm25Grade="
				+ pm25Grade + ", o3Value=" + o3Value + ", o3Grade=" + o3Grade + ", coValue=" + coValue + ", coGrade="
				+ coGrade + ", no2Value=" + no2Value + ", no2Grade=" + no2Grade + ", so2Value=" + so2Value
				+ ", so2Grade=" + so2Grade + "]";
	}
    
    
}