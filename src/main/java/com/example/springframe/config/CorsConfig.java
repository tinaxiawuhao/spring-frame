package com.example.springframe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //配置允许跨域访问的路径
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                // 允许提交请求的方法，*表示全部允许
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Origin")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptor) {
        interceptor.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
    }

}
