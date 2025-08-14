package org.group4.dvdshopbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // application.properties 파일 필수
    //#파일 한 개당 최대 사이즈
    //spring.servlet.multipart.maxFileSize=20MB
    //#요청당 최대 파일 크기
    //spring.servlet.multipart.maxRequestSize=100MB
    //#상품 이미지 업로드 경로
    //itemImgLocation=C:/shop/item
    //#리소스 업로드 경로
    //uploadPath=file:///C:/shop/
    //
    //#기본 batch size 설정
    //spring.jpa.properties.hibernate.default_batch_fetch_size=1000

    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 웹 브라우져에 입력하는 url에 /images로 시작하는 경우 로컬 컴퓨터에 저장된 파일 읽어올 경로
        // service.FileService에 파일 처리용 클래스 생성
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }


    @Configuration
    public class WebCorsConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**") // API만 허용 (모두 열려면 "/**")
                    .allowedOrigins("http://localhost:3000", "http://localhost:3001")
                    .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                    .allowedHeaders("*")
                    .exposedHeaders("*")
                    .allowCredentials(false); // 인증쿠키 필요하면 true + allowedOrigins에 * 금지
        }
    }


}
