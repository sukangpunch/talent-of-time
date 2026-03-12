package com.example.talentoftime.cache;

import com.example.talentoftime.cache.annotation.DefaultCacheOut;
import com.example.talentoftime.cache.annotation.DefaultCaching;
import com.example.talentoftime.cache.manager.CacheManager;
import com.example.talentoftime.cache.util.CacheKeyResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CachingAspect {

    private final ApplicationContext applicationContext;
    private final CacheKeyResolver cacheKeyResolver; // 분리된 키 해석기 주입!

    @Around("@annotation(defaultCaching)")
    public Object cache(ProceedingJoinPoint joinPoint, DefaultCaching defaultCaching) throws Throwable {

        CacheManager cacheManager = (CacheManager) applicationContext.getBean(defaultCaching.cacheManager());
        String key = cacheKeyResolver.resolve(defaultCaching.key(), joinPoint);
        Long ttl = defaultCaching.ttlSec();

        // 1. 캐시에 있으면 반환
        Object cachedValue = cacheManager.get(key);
        if (cachedValue != null) {
            log.info("[Cache HIT] key={}, method={}", key, joinPoint.getSignature().toShortString());
            return cachedValue;
        }
        // 2. 캐시에 없으면 캐싱 후 반환
        log.info("[Cache MISS] key={}, method={}", key, joinPoint.getSignature().toShortString());
        Object result = joinPoint.proceed();
        cacheManager.put(key, result, ttl);
        return result;
    }

    @Around("@annotation(defaultCacheOut)")
    public Object cacheEvict(ProceedingJoinPoint joinPoint, DefaultCacheOut defaultCacheOut) throws Throwable {

        CacheManager cacheManager = (CacheManager) applicationContext.getBean(defaultCacheOut.cacheManager());

        // 1. 실제 비즈니스 로직(db 쓰기/수정/석제) 을 먼저 실행하고 결과를 저장
        Object result = joinPoint.proceed();

        // 2. db 업데이트가 완료된 후, 안전하게 캐시를 무효화(삭제)
        for (String key : defaultCacheOut.key()) {
            String cacheKey = cacheKeyResolver.resolve(key, joinPoint);
            boolean usingPrefix = defaultCacheOut.prefix();

            if (usingPrefix) {
                cacheManager.evictUsingPrefix(cacheKey);
            } else {
                cacheManager.evict(cacheKey);
            }
            log.info("[Cache EVICT] key={}, method={}", cacheKey, joinPoint.getSignature().toShortString());
        }
        
        // 3. 처음에 저장해둔 비즈니스 로직의 결과 반환
        // 해당 작업으로 캐시 삭제와 동시에 조회 요청이 들어올 때, 오래된 캐시 삭제 후 다시 오래된 캐시등록하는 레이스 컨디션 문제를 방지할 수 있다.
        return result;
    }
}
