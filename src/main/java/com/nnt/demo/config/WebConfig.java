package com.nnt.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", (clazz)->true);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("**").allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
