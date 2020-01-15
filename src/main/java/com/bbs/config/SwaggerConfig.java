package com.bbs.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    Docket docket(ApplicationContext applicationContext) {
        String activeProfile = applicationContext.getEnvironment().getActiveProfiles()[0];

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .enable("dev".equals(activeProfile))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.own.web.controller"))
                .paths(PathSelectors.regex("((?!Error).)*"))
                .build()
                .tags(new Tag("版块控制器", "获取版块信息"), getTags());
        Contact contact = new Contact("clx", "", "");
        ApiInfo apiInfo = new ApiInfo("bbs API文档", "Api Documentation", "1.0", "", contact, "Apache 2.0", "", new ArrayList());

        return docket.apiInfo(apiInfo);
    }

    private Tag[] getTags() {
        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("用户控制器", "用户登录，注册，操作账号信息等操作"));
        tagList.add(new Tag("消息控制器", "操作用户收到的回复消息"));
        tagList.add(new Tag("回复控制器", "查看，发布，删除回复或楼中楼"));
        tagList.add(new Tag("帖子控制器", "操作帖子数据"));
        return tagList.toArray(new Tag[0]);
    }
}
