package com.example.talentoftime.auth.strategy;

import com.example.talentoftime.auth.dto.oauth.OAuthUserInfo;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OAuthResponseFactory {
    private final Map<String, OAuthResponseStrategy> strategies;

    public OAuthResponseFactory(List<OAuthResponseStrategy> strategyList)
    {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(OAuthResponseStrategy::getProviderName, Function.identity()));
    }

    public OAuthUserInfo createOAuthUserInfo(
            String registrationId,
            Map<String, Object> attributes
    ){
        OAuthResponseStrategy strategy = strategies.get(registrationId);
        if(Objects.equals(strategy, null))
            throw new BusinessException(ErrorCode.PROVIDER_INVALID);
        return strategy.createOAuthUserInfo(attributes);
    }
}
