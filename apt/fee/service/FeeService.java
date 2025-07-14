package com.project.apt.fee.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.apt.fee.dao.IFeeDAO;
import com.project.apt.fee.vo.FeeAptAddressVO;
import com.project.apt.fee.vo.FeeElecVO;

@Service
public class FeeService {

	@Autowired
	IFeeDAO dao;
	
	// 구 불러오기
	public List<String> getDistrictList() {
		return dao.getDistrictList();
	}
	
	// 동 불러오기
	public List<String> getDongList(String district){
		return dao.getDongList(district);
	}
	 
	// 년도 불러오기
	public List<String> getYearList(){
		return dao.getYearList();
	}
	
	// 월 불러오기
	public List<String> getMonthList(int year){
		return dao.getMonthList(year);
	}
	
	// 아파트 목록 불러오기
	public List<FeeAptAddressVO> getAptList(String dong){
		return dao.getAptList(dong);
	}
	
	// 관리비 불러오기
	public int getMgmtFee(String kaptCode, int year, int month) {
		return dao.getMgmtFee(kaptCode, year, month);
	}
	
	// 전기요금 불러오기
	public FeeElecVO getElecFee(String kaptCode, int year, int month) {
		return dao.getElecFee(kaptCode, year, month);
	}
}
