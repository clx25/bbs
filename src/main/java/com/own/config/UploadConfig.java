package com.own.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class UploadConfig {

    @Bean
    public CommonsMultipartResolver commonsMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        //设置上传文件的最大上传大小
        multipartResolver.setMaxUploadSize(4194304);
        return multipartResolver;
    }
}
