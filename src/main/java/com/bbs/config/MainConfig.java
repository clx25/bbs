package com.bbs.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.bbs.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.concurrent.*;

@Configuration

@EnableAspectJAutoProxy
@EnableAsync
@MapperScan("com.bbs.mapper")
public class MainConfig {


    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        //该组件使jsr303注解支持形参和返回值的校验
        return new MethodValidationPostProcessor();
    }

}
