package com.example.talentoftime.cache.util;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheKeyResolver {

    private final SpelExpressionParser spelParser = new SpelExpressionParser();

    public String resolve(String keyExpression, ProceedingJoinPoint joinPoint) {
        // 1. 레거시 치환 방식: {0}, {1}
        if (keyExpression.contains("{") && keyExpression.contains("}")) {
            return resolveLegacyPlaceholder(keyExpression, joinPoint.getArgs());
        }

        // 2. SpEL 동적 변수 방식: #date, #request.id
        if (keyExpression.contains("#")) {
            return resolveSpelWithVariables(keyExpression, joinPoint);
        }

        // 3. SpEL 정적 호출 & 리터럴 방식: T(java.time.LocalDate).now(), 'prefix'
        if (keyExpression.contains("T(") || keyExpression.contains("'")) {
            return resolveStaticSpel(keyExpression);
        }

        // 4. 어떤 기호도 없는 순수 문자열 (고정 키)
        return keyExpression;
    }

    /**
     * 타입 1: {0} 형태의 레거시 파라미터 인덱스 치환
     */
    private String resolveLegacyPlaceholder(String keyPattern, Object[] args) {
        if (args == null || args.length == 0) {
            return keyPattern;
        }

        for (int i = 0; i < args.length; i++) {
            String placeHolder = "{" + i + "}";
            if (keyPattern.contains(placeHolder)) {
                String replacement = (args[i] != null) ? args[i].toString() : "null";
                keyPattern = keyPattern.replace(placeHolder, replacement);
            }
        }
        return keyPattern;
    }

    /**
     * 타입 2: # 파라미터 변수가 포함된 SpEL 해석 (파라미터 바인딩 필수)
     * 예: "'class-session:' + #date"
     */
    private String resolveSpelWithVariables(String keyExpression, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        StandardEvaluationContext context = new StandardEvaluationContext();

        // #변수 매핑을 위해 메서드의 파라미터 이름과 실제 값을 Context에 바인딩
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        return spelParser.parseExpression(keyExpression).getValue(context, String.class);
    }

    /**
     * 타입 3: T() 정적 클래스 호출이나 '문자열'만 포함된 SpEL 해석 (성능 최적화)
     * 예: "'class-session:' + T(java.time.LocalDate).now()"
     */
    private String resolveStaticSpel(String keyExpression) {
        // 파라미터를 읽고 바인딩하는 무거운 과정 생략! 빈 Context만으로 즉시 해석
        StandardEvaluationContext context = new StandardEvaluationContext();
        return spelParser.parseExpression(keyExpression).getValue(context, String.class);
    }
}

