package com.project.apt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAsync
public class AptMk2Application {

	public static void main(String[] args) {
		SpringApplication.run(AptMk2Application.class, args);
	}
	
	@Bean // 이 메서드가 반환하는 객체를 Spring 컨테이너의 빈으로 등록
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
