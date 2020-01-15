package com.bbs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于标识缓存用的cacheName,key,返回值类型，【未使用】
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheSetting {
    //cache组件名称
    String cacheName();

    //缓存的key
    String key() default "";

    //保存的值类型
    Class<?> CLASS();

}
