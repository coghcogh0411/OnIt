package com.minho.ownit;

import org.springframework.context.annotation.Configuration;
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
}
