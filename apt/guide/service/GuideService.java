package com.project.apt.guide.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.apt.guide.dao.IGuideDAO;
import com.project.apt.guide.vo.*;

@Service
public class GuideService {

    @Autowired
    private IGuideDAO guideDAO;

    private final RestTemplate restTemplate = new RestTemplate();

    /** 아파트명 검색 */
    public List<String> findAptNames(String term) {
        return guideDAO.findAptNames(term);
    }

    /** 아파트명으로 블록 조회 */
    public List<String> findBlocksByAptName(String aptName) {
        return guideDAO.findBlocksByAptName(aptName);
    }

    /** 아파트 + 블록으로 상세 주소 검색 */
    public Map<String, Object> searchAddress(String name, String block) {
        GuideVO vo = guideDAO.searchAddress(name, block);
        Map<String, Object> result = new HashMap<>();
        if (vo == null) {
            result.put("success", false);
            result.put("message", "검색 결과 없음");
        } else {
            result.put("success", true);
            result.put("kaptCode", vo.getKaptCode());
            result.put("kaptName", vo.getKaptName());
            result.put("aptBlock", vo.getAptBlock());
            result.put("latitude", vo.getLatitude());
            result.put("longitude", vo.getLongitude());
            result.put("direction", vo.getDirection());
            result.put("newAddress", vo.getNewAddress());
            result.put("dong", vo.getDong());
        }
        return result;
    }

    /** 유저의 기본 아파트 정보 */
    public GuideVO getUserAptInfo(String userId) {
        return guideDAO.getUserAptInfo(userId);
    }
    
    private String convertDustGradeToEmoji(String dustGrade) {
        if (dustGrade == null) return "";
        
        return dustGrade.replace("좋음", "😊")
                        .replace("보통", "😕")
                        .replace("나쁨", "😡");
    }

    /** Flask에서 주간 날씨 데이터 조회 */
    public List<WeeklyWeatherVO> getWeeklyWeatherFromFlask(String dongName) {
        String flaskApiUrl = "http://192.168.0.17/api/weekly_weather?dong=" + dongName;
        try {
            WeeklyWeatherVO[] response = restTemplate.getForObject(flaskApiUrl, WeeklyWeatherVO[].class);
            return (response != null) ? Arrays.asList(response) : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("[GuideService] Flask 호출 실패: " + flaskApiUrl);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /** 주간 날씨 데이터 요약 (일별 평균 + 추천 시간대 요약) */
    public List<DailySummaryVO> summarizeWeeklyWeather(List<WeeklyWeatherVO> weeklyWeather) {
        Map<String, List<WeeklyWeatherVO>> groupedByDate = new HashMap<>();
        for (WeeklyWeatherVO vo : weeklyWeather) {
            groupedByDate.computeIfAbsent(vo.getDate(), k -> new ArrayList<>()).add(vo);
        }

        List<DailySummaryVO> summaries = new ArrayList<>();

        for (Map.Entry<String, List<WeeklyWeatherVO>> entry : groupedByDate.entrySet()) {
            String date = entry.getKey();
            List<WeeklyWeatherVO> dailyList = entry.getValue();

            double sumTemp = 0, sumHumidity = 0, sumWindSpeed = 0;
            int validDataCount = 0;

            List<Integer> recommendTimes = new ArrayList<>();
            List<Integer> normalTimes = new ArrayList<>();
            List<Integer> notRecommendTimes = new ArrayList<>();
            String dustGrade = "예보 없음";

            for (WeeklyWeatherVO vo : dailyList) {
                try {
                    sumTemp += Double.parseDouble(vo.getTemperature());
                    sumHumidity += Double.parseDouble(vo.getHumidity());
                    sumWindSpeed += Double.parseDouble(vo.getWindSpeed());
                    validDataCount++;

                    int hour = Integer.parseInt(vo.getTime().substring(0, 2));
                    String vent = vo.getVentRecommend();
                    if ("추천".equals(vent) || "매우 추천".equals(vent)) {
                        recommendTimes.add(hour);
                    } else if ("보통".equals(vent)) {
                        normalTimes.add(hour);
                    } else if ("비추천".equals(vent)) {
                        notRecommendTimes.add(hour);
                    }

                    if (vo.getDustGrade() != null && !vo.getDustGrade().equals("-") && !vo.getDustGrade().equals("예보 없음")) {
                        dustGrade = vo.getDustGrade();
                    }
                } catch (NumberFormatException e) {
                    System.err.println("잘못된 숫자 형식 데이터 무시: " + vo);
                }
            }

            if (validDataCount == 0) continue;

            double avgTemp = Math.round(sumTemp / validDataCount * 10) / 10.0;
            double avgHumidity = Math.round(sumHumidity / validDataCount * 10) / 10.0;
            double avgWindSpeed = Math.round(sumWindSpeed / validDataCount * 10) / 10.0;

            DailySummaryVO summary = new DailySummaryVO();
            summary.setDate(date);
            summary.setDateDisplay(formatDateWithDay(date));
            summary.setAvgTemperature(avgTemp);
            summary.setAvgHumidity(avgHumidity);
            summary.setAvgWindSpeed(avgWindSpeed);
            summary.setDryingGrade(evaluateDryingGrade(avgTemp, avgHumidity, avgWindSpeed));

            // [수정] 이모티콘 변환 후 <br> 적용
            if (dustGrade != null) {
                String emojiGrade = convertDustGradeToEmoji(dustGrade);
                summary.setDustGrade(emojiGrade.replaceAll(",\\s*", "<br>"));
            } else {
                summary.setDustGrade("예보 없음");
            }

            String ventStatus = (notRecommendTimes.size() > recommendTimes.size() + normalTimes.size()) ? "비추천" : "추천";
            summary.setVentStatus(ventStatus);

            summaries.add(summary);
        }

        summaries.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return summaries;
    }

    /** Flask에서 주간 환기 데이터 조회 */
    public List<WeeklyVentilationVO> getWeeklyVentilationFromFlask(String dongName) {
        String flaskApiUrl = "http://192.168.0.17/api/weekly_ventilation?dong=" + dongName;
        try {
            WeeklyVentilationVO[] response = restTemplate.getForObject(flaskApiUrl, WeeklyVentilationVO[].class);
            if (response != null && response.length > 0) {
                List<WeeklyVentilationVO> ventilationList = new ArrayList<>(Arrays.asList(response));

                for (WeeklyVentilationVO vo : ventilationList) {
                    vo.setDryingGrade(evaluateDryingGrade(vo.getTempAvg(), vo.getHumiAvg(), vo.getWindAvg()));
                 // [수정] dustReason의 텍스트를 이모티콘으로 변환
                    if (vo.getDustReason() != null) {
                        vo.setDustReason(convertDustGradeToEmoji(vo.getDustReason()));
                    }
                }

                ventilationList.sort((a, b) -> b.getDate().compareTo(a.getDate()));
                return ventilationList;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[GuideService] Flask 호출 실패: " + flaskApiUrl);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /** yyyyMMdd → "M월 d일(요일)" 변환 */
    private String formatDateWithDay(String dateStr) {
        DateTimeFormatter formatter = dateStr.contains("-") ?
                DateTimeFormatter.ofPattern("yyyy-MM-dd") : DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String koreanDay = switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
        return String.format("%d월 %d일(%s)", date.getMonthValue(), date.getDayOfMonth(), koreanDay);
    }

    /** 건조 적합도 평가 로직 */
    private String evaluateDryingGrade(double temp, double humi, double wind) {
        int count = 0;
        if (temp >= 25) count++;
        if (humi <= 50) count++;
        if (wind >= 2.0) count++;
        if (count == 3) return "매우 추천";

        count = 0;
        if (temp >= 20) count++;
        if (humi <= 60) count++;
        if (wind >= 1.5) count++;
        if (count >= 2) return "추천";

        count = 0;
        if (temp >= 15) count++;
        if (humi <= 70) count++;
        if (wind >= 1.0) count++;
        if (count >= 2) return "보통";

        count = 0;
        if (temp >= 10) count++;
        if (humi <= 80) count++;
        if (wind >= 0.5) count++;
        if (count >= 1) return "비추천";

        return "매우 비추천";
    }
}