package com.minho.ownit;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /HoMini/Img/** 로 들어오는 요청을
        // file:///C:/HoMini/Img/ 에 매핑
    	
        registry.addResourceHandler("/HoMini/Img/**")
                .addResourceLocations("file:///C:/HoMini/Img/");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
        .allowedOrigins("*") // 허용할 도메인
        .allowedMethods("GET", "POST") // 허용할 HTTP 메소드
        .allowedHeaders("*"); // 모든 헤더 허용
    }
}
