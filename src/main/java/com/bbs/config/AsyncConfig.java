package com.bbs.config;

import com.bbs.exception.GlobalExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        //配置捕获@Async注解的多线程中的异常处理器
        return new GlobalExceptionHandler.MyAsyncUncaughtExceptionHandler();
    }
}
