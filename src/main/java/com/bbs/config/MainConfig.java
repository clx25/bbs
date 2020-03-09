package com.bbs.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@EnableAsync
@EnableScheduling
@MapperScan("com.bbs.mapper")
public class MainConfig {


    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {

        //该组件使jsr303注解支持形参和返回值的校验
        return new MethodValidationPostProcessor();
    }


    @Bean
    public TaskScheduler taskScheduler(){
        //解决websocket与定时任务冲突。
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
