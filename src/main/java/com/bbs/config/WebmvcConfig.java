package com.bbs.config;

import com.bbs.interceptor.DelPermissionCheckInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebmvcConfig implements WebMvcConfigurer {

    private final DelPermissionCheckInterceptor delPermissionCheckInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(delPermissionCheckInterceptor).addPathPatterns("/reply/*");
    }
}
