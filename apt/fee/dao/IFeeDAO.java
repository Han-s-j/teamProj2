package com.project.apt.fee.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.apt.fee.vo.FeeAptAddressVO;
import com.project.apt.fee.vo.FeeCompareVO;
import com.project.apt.fee.vo.FeeElecVO;
import com.project.apt.fee.vo.FeeVO;

@Mapper
public interface IFeeDAO {
	
	// user ID 불러와 저장
	void insertFee(FeeVO fee);
	
	// 유저의 개인 관리비 가져오기
	List<FeeCompareVO> selectUserAndAvgFees(String userId);
	
	// ===================== 비교 페이지 =====================
	// 구 불러오기(전기세 비교 페이지)
	public List<String> getDistrictList();

	// 동 불러오기(전기세 비교 페이지)
	public List<String> getDongList(String district);
	
	// 비교 년도 불러오기(전기세 비교 페이지)
	public List<String> getYearList();
	
	// 비교 월 불러오기(전기세 비교 페이지)
	public List<String> getMonthList(@Param("year") int year);
	
	// 아파트 목록 불러오기
	public List<FeeAptAddressVO> getAptList(@Param("dong") String dong);
	
	// 관리비 불러오기
	Integer getMgmtFee(@Param("kaptCode") String kaptCode,
		               @Param("year") int year,
		               @Param("month") int month);
	
	// 전기요금 불러오기
	public FeeElecVO getElecFee(@Param("kaptCode") String kaptCode,
					            @Param("year") int year,
					            @Param("month") int month);
}
