package com.bbs.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisUtil {


    private static RedisTemplate<Object, Object> redisTemplate;


    @Resource
    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }


    public static Object hget(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public static void hdel(String key, Object hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    public static <T> void hset(String key, Object hashKey, T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }


    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }



}
