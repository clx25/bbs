package com.own;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
public class BbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BbsApplication.class, args);
    }

}
