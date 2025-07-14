package com.project.apt.user.vo;

import java.util.Map;

import lombok.Data;

@Data
public class OcrResultVO {
	 // Python 서버의 응답 필드에 맞게 구성
    // Python 서버에서 'message' 안에 dong_result, ho_result, number_result가 있다면 Map으로 받습니다.
    private Map<String, String> message;
    private String image; // Base64 인코딩된 이미지

    // 편의를 위한 Getter (message 맵에서 값을 직접 가져옴)
    public String getDong_result() {
        return message != null ? message.get("dong_result") : null;
    }

    public String getHo_result() {
        return message != null ? message.get("ho_result") : null;
    }

    public String getNumber_result() {
        return message != null ? message.get("number_result") : null;
    }
}
