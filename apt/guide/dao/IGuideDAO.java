package com.project.apt.guide.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.apt.guide.vo.AirQualityVO; // AirQualityVO 임포트 추가
import com.project.apt.guide.vo.GuideVO;
import com.project.apt.guide.vo.PastWeatherAirVO;

@Mapper
public interface IGuideDAO {

	// 아파트명 자동완성
    List<String> findAptNames(String term);

    // 블록(동) 목록 조회
    List<String> findBlocksByAptName(String aptName);

    // 아파트 정보 및 좌표 검색 (block이 null이면 아파트명만)
    GuideVO searchAddress(@Param("name") String name, @Param("block") String block);

    // 로그인 유저의 아파트 정보 조회
    GuideVO getUserAptInfo(String userId);

    // KAPT_CODE로 아파트 정보 조회 (위도, 경도, 동 이름 등 포함)
    GuideVO getAptInfoByKaptCode(@Param("kaptCode") String kaptCode);

    // [추가] 측정소명으로 최신 대기질 정보 조회 (air_quality_log 테이블 활용)
    // air_quality_log 테이블의 스키마 (컬럼명 및 데이터 타입)에 따라 쿼리 및 매핑이 달라질 수 있습니다.
    AirQualityVO getLatestAirQualityByStation(@Param("stationName") String stationName);
    
    List<PastWeatherAirVO> getPastWeatherAirByDong(String dongName);

}