package com.example.talentoftime.cache.manager;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component("customCacheManager")
@RequiredArgsConstructor
public class CustomCacheManager implements CacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    public void put(String key, Object object, Long ttl) {
        redisTemplate.opsForValue().set(key, object, Duration.ofSeconds(ttl));
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);    }

    public void evict(String key) {
        redisTemplate.delete(key);
    }

    public void evictUsingPrefix(String key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void evictMultiple(List<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
