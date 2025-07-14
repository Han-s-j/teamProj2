package com.project.apt.challenge.web;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.apt.challenge.dao.BadgeDAO;
import com.project.apt.challenge.vo.BadgeInfoVO;

@Controller
public class BadgeController {

    @Autowired
    private BadgeDAO badgeDAO;

    // 뱃지 이미지 제공 API (BLOB → 바이트 배열) - 개선된 버전
    @GetMapping("/badge/image/{badgeId}")
    @ResponseBody
    public ResponseEntity<byte[]> getBadgeImage(@PathVariable String badgeId) {
        try {
            System.out.println("뱃지 이미지 요청: " + badgeId);
            
            BadgeInfoVO badgeInfo = badgeDAO.getBadgeInfo(badgeId);
            
            if (badgeInfo == null) {
                System.out.println("뱃지 정보 없음: " + badgeId);
                return ResponseEntity.notFound().build();
            }
            
            byte[] blob = badgeInfo.getBadgeImg();
            if (blob == null) {
                System.out.println("뱃지 이미지 BLOB 없음: " + badgeId);
                return ResponseEntity.notFound().build();
            }
            

            if (blob == null || blob.length == 0) {
                System.out.println("이미지 바이트 배열이 비어있음: " + badgeId);
                return ResponseEntity.notFound().build();
            }
            
            HttpHeaders headers = new HttpHeaders();
            
            // 이미지 타입 자동 감지 (개선된 부분)
            String contentType = detectImageType(blob);
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(blob.length);
            headers.setCacheControl("max-age=3600"); // 1시간 캐시
            
            // CORS 헤더 추가
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET");
            
            System.out.println("뱃지 이미지 반환 성공: " + badgeId + 
                             ", 크기: " + blob.length + 
                             ", 타입: " + contentType);
            
            return new ResponseEntity<>(blob, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            System.err.println("뱃지 이미지 일반 오류: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 이미지 타입 자동 감지 메서드 (새로 추가)
    private String detectImageType(byte[] imageBytes) {
        if (imageBytes.length < 8) {
            return "image/png"; // 기본값
        }
        
        // PNG 시그니처: 89 50 4E 47 0D 0A 1A 0A
        if (imageBytes[0] == (byte) 0x89 && imageBytes[1] == 0x50 && 
            imageBytes[2] == 0x4E && imageBytes[3] == 0x47) {
            return "image/png";
        }
        
        // JPEG 시그니처: FF D8 FF
        if (imageBytes[0] == (byte) 0xFF && imageBytes[1] == (byte) 0xD8 && 
            imageBytes[2] == (byte) 0xFF) {
            return "image/jpeg";
        }
        
        // GIF 시그니처: 47 49 46
        if (imageBytes[0] == 0x47 && imageBytes[1] == 0x49 && imageBytes[2] == 0x46) {
            return "image/gif";
        }
        
        // WebP 시그니처: 52 49 46 46 ... 57 45 42 50
        if (imageBytes.length >= 12 && imageBytes[0] == 0x52 && imageBytes[1] == 0x49 && 
            imageBytes[2] == 0x46 && imageBytes[3] == 0x46 &&
            imageBytes[8] == 0x57 && imageBytes[9] == 0x45 && 
            imageBytes[10] == 0x42 && imageBytes[11] == 0x50) {
            return "image/webp";
        }
        
        // 기본값 PNG
        return "image/png";
    }

    // 뱃지 정의 목록 조회 API
    @PostMapping("/badge/getBadgeDefinitions")
    @ResponseBody
    public java.util.Map<String, Object> getBadgeDefinitions() {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        
        try {
            var badges = badgeDAO.getBadgeDefinitions();
            System.out.println("뱃지 정의 조회 성공, 개수: " + badges.size());
            response.put("success", true);
            response.put("badges", badges);
        } catch (Exception e) {
            System.err.println("뱃지 정의 조회 실패: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        
        return response;
    }
}