package com.project.apt.guide.vo;

import lombok.Data; // Lombok을 사용한다면 이 줄을 추가

// 미세먼지 정보를 담을 VO (Value Object) 클래스
@Data // @Data 어노테이션은 @Getter, @Setter, @ToString 등을 자동으로 생성해 줍니다.
public class FineDustVO {
    private String stationName; // 측정소명
    private String dataTime;    // 측정 시간
    private Double pm10Value;   // 미세먼지 농도
    private Double pm25Value;   // 초미세먼지 농도
    private String pm10Grade;   // 미세먼지 등급 (숫자 -> 문자열로 변경하는 것이 일반적)
    private String pm25Grade;   // 초미세먼지 등급
    private String khaiGrade;   // 통합대기환경지수 등급


}