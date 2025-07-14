package com.project.apt.guide.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.apt.guide.service.GuideService;
import com.project.apt.guide.vo.DailySummaryVO;
import com.project.apt.guide.vo.GuideVO;
import com.project.apt.guide.vo.WeeklyVentilationVO;
import com.project.apt.guide.vo.WeeklyWeatherVO; // WeeklyWeatherVO 임포트
import com.project.apt.user.vo.UserVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class GuideController {

	@Autowired
	private GuideService guideService;

	/**
	 * [1] 회원 아파트 정보 + 닉네임 등 DB에서 바로 조회 (Flask 없이!)
	 */
	@GetMapping("/guideDetail")
	public String guideDetail(Model model, HttpSession session) {
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		String userId = null;
		if (loginUser != null) {
			userId = loginUser.getUserId();
		}

		GuideVO aptInfo = null;
		if (userId != null) {
			aptInfo = guideService.getUserAptInfo(userId);
		}
		if (aptInfo != null) {
			model.addAttribute("aptInfo", aptInfo);
			model.addAttribute("nickname", aptInfo.getNickname());
			model.addAttribute("latitude", aptInfo.getLatitude());
			model.addAttribute("longitude", aptInfo.getLongitude());
		} else {
			// 로그인 안함, 정보없을때
			model.addAttribute("nickname", "Guest");
			model.addAttribute("latitude", 36.3504);
			model.addAttribute("longitude", 127.3845);
		}

		return "guide/guideDetail";
	}

	/**
	 * [가이드 메인 페이지] 로그인 사용자 기반 주간 날씨 조회 + 요약 정보 포함
	 */
	@GetMapping("/guideMain")
	public String guideMain(Model model, HttpSession session) {
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		String userId = (loginUser != null) ? loginUser.getUserId() : null;

		GuideVO userAptInfo = null;
		String userDongName = null;
		String userNickname = "게스트";
		Double latitude = 36.3504;
		Double longitude = 127.3845;

		if (userId != null) {
			userAptInfo = guideService.getUserAptInfo(userId);
			if (userAptInfo != null) {
				userDongName = userAptInfo.getDong();
				userNickname = userAptInfo.getNickname();
				if (userAptInfo.getLatitude() != null)
					latitude = userAptInfo.getLatitude();
				if (userAptInfo.getLongitude() != null)
					longitude = userAptInfo.getLongitude();
			}
		}

		if (userAptInfo == null)
			userAptInfo = new GuideVO();
		userAptInfo.setNickname(userNickname);
		userAptInfo.setLatitude(latitude);
		userAptInfo.setLongitude(longitude);
		userAptInfo.setDong(userDongName != null ? userDongName : "정보 없음");

		model.addAttribute("userAptInfo", userAptInfo);
		model.addAttribute("userNickname", userNickname);

		List<WeeklyWeatherVO> weeklyWeather = new ArrayList<>();
		String weatherDongName = "정보 없음";

		if (userDongName != null && !userDongName.isEmpty() && !"정보 없음".equals(userDongName)) {
			try {
				System.out.println("[GuideController] Flask 호출 시작: " + userDongName);
				weeklyWeather = guideService.getWeeklyWeatherFromFlask(userDongName);
				System.out.println("[GuideController] Flask 응답 결과: " + weeklyWeather);

				weatherDongName = userDongName;

				if (weeklyWeather.isEmpty()) {
					System.out.println("[GuideController] Flask 응답이 빈 배열입니다. dong: " + userDongName);
				}

				// ✅✅ 추가된 부분 – 날짜별 요약 리스트 생성
				List<DailySummaryVO> dailySummaries = guideService.summarizeWeeklyWeather(weeklyWeather);

				// ✅ dustGrade를 하루 단위로 동일하게 세팅
				if (!weeklyWeather.isEmpty()) {
					// 🔥 날짜별 dustGrade 매핑 생성
					Map<String, String> dateToDustGrade = new HashMap<>();
					for (WeeklyWeatherVO weather : weeklyWeather) {
						String date = weather.getDate();
						String dustGrade = weather.getDustGrade();
						// 처음 등장하는 날짜의 dustGrade만 저장 (null이 아니면)
						if (!dateToDustGrade.containsKey(date) && dustGrade != null) {
							dateToDustGrade.put(date, dustGrade);
						}
					}

					// 🔥 요약 리스트에 날짜별 dustGrade 세팅
					for (DailySummaryVO summary : dailySummaries) {
						String date = summary.getDate();
						String dustGrade = dateToDustGrade.getOrDefault(date, "예보 없음");
						summary.setDustGrade(dustGrade);

						// 🔥 formattedDate 세팅
						if (date != null && date.length() == 8) {
							String formattedDate = date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
									+ date.substring(6, 8);
							summary.setFormattedDate(formattedDate);
						} else {
							summary.setFormattedDate(date);
						}
					}
				}

				model.addAttribute("dailySummaries", dailySummaries);

				// ✅✅ 과거 환기 추천 DB 기반 조회
				List<WeeklyVentilationVO> weeklyVentilation = guideService.getWeeklyVentilationFromFlask(userDongName);
				System.out.println("[GuideController] weeklyVentilation size = " + weeklyVentilation.size());
				model.addAttribute("weeklyVentilation", weeklyVentilation);
			} catch (Exception e) {
				System.err.println("[GuideController] Flask 호출 오류: " + e.getMessage());
				e.printStackTrace();
			}
		}

		model.addAttribute("weeklyWeather", weeklyWeather);
		model.addAttribute("weatherDongName", weatherDongName);

		return "guide/guideMain";
	}

	/**
	 * [2] 기본 일조량 분석 페이지 (지도/분석 폼)
	 */
	@GetMapping("/guide/sunlight")
	public String sunlightMap(Model model) {
		model.addAttribute("latitude", 36.3504);
		model.addAttribute("longitude", 127.3845);
		return "sunlightMap";
	}

	/**
	 * [3] 아파트명 자동완성 (최대 10개)
	 */
	@GetMapping("/autocomplete_name")
	@ResponseBody
	public List<String> autocompleteName(@RequestParam String term) {
		return guideService.findAptNames(term);
	}

	/**
	 * [4] 아파트 동(블록) 목록 조회
	 */
	@GetMapping("/get_blocks")
	@ResponseBody
	public List<String> getBlocks(@RequestParam("apt_name") String aptName) {
		List<String> blocks = guideService.findBlocksByAptName(aptName);
		blocks.sort((a, b) -> {
			try {
				int numA = Integer.parseInt(a.replaceAll("[^0-9]", ""));
				int numB = Integer.parseInt(b.replaceAll("[^0-9]", ""));
				return numA - numB;
			} catch (NumberFormatException e) {
				return a.compareTo(b);
			}
		});
		return blocks;
	}

	/**
	 * [5] 아파트명+동(블록)으로 좌표/정보 조회
	 */
	@PostMapping("/search_address")
	@ResponseBody
	public Map<String, Object> searchAddress(@RequestBody Map<String, String> params) {
		String name = params.get("name");
		String block = params.get("block");
		return guideService.searchAddress(name, block);
	}

}