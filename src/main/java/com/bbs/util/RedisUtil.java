package com.bbs.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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

    public static void linsert(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public static List<Object> lrange(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public static long llen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public static void trim(String key,int start,int end){
        redisTemplate.opsForList().trim(key,start,end);
    }
}
