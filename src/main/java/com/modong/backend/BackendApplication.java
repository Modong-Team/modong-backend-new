package com.modong.backend;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BackendApplication {
	@PostConstruct
	public void setTime(){ // 서버의 기본 시간을 한국 표준시인 KST로 변경
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
