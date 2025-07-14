package com.project.apt.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ICommonDAO {

	// 전체 유저 수 가져오기
	public int getAllUserCount();
	
	// 전체 포인트 가져오기
	public int getUserPoint();
	
	// 전체 게시글 수 가져오기
	public int getAllBoardCount();
	// 새로 추가할 차트용 메서드들
		public int getTodayVisitors();
		public int getTodaySignups();
		public int getTodayWithdrawals();
		public List<Map<String, Object>> getVisitorsChartData();
		public List<Map<String, Object>> getUserChangeChartData();
}
