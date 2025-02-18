package com.minho.ownit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.properties에서 ho.img.folder 값을 주입받음
    @Value("${ho.img.folder}")
    private String imgFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /HoMini/Img/** 로 들어오는 요청을
        // file:///C:/HoMini/Img/ 에 매핑
        registry.addResourceHandler("/HoMini/Img/**")
                .addResourceLocations("file:///C:/HoMini/Img/");
    }
}
