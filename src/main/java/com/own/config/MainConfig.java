package com.own.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration

@EnableAspectJAutoProxy
@MapperScan("com.own.mapper")
public class MainConfig {


    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        //该组件使jsr303注解支持形参和返回值的校验
        return new MethodValidationPostProcessor();
    }
}
