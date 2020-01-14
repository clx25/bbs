package com.own.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.own.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.concurrent.*;

@Configuration

@EnableAspectJAutoProxy
@MapperScan("com.own.mapper")
public class MainConfig {


    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        //该组件使jsr303注解支持形参和返回值的校验
        return new MethodValidationPostProcessor();
    }

    @Bean
    public ExecutorService pool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler(new GlobalExceptionHandler.MyUncaughtExceptionHandler())
                .build();

        return new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), threadFactory,new ThreadPoolExecutor.AbortPolicy());

    }
}
