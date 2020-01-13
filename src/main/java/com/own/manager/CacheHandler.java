package com.own.manager;

import com.own.annotation.CacheSetting;
import com.own.servie.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component

/**
 * 通用的缓存处理器，【未使用】
 */
public class CacheHandler {

    private static final String USER_CACHE_NAME = "user";

    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private UserService userService;
    private SpelExpressionParser parser=new SpelExpressionParser();

    @Around("@annotation(CacheSetting)")
    public Object cacheHandler(ProceedingJoinPoint joinPoint, CacheSetting CacheSetting) throws Throwable {
        log.info("缓存处理器执行了");
        String cacheName = CacheSetting.cacheName();
        String key = CacheSetting.key();
        if("".equals(key)){
//            key=userService.getLoginPrincipal();
        }
        SpelExpression spelExpression = parser.parseRaw(key);
        String k = spelExpression.getExpressionString();
        Cache cache = redisCacheManager.getCache(cacheName);
        Object result = cache.get(key, CacheSetting.CLASS());
        if(result==null){
            result = joinPoint.proceed(joinPoint.getArgs());
            cache.put(key,result);
        }

        return result;
    }


}
