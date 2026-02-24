package com.example.talentoftime.auth.strategy;

import com.example.talentoftime.auth.dto.oauth.KakaoUserInfo;
import com.example.talentoftime.auth.dto.oauth.OAuthUserInfo;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuthResponseStrategy implements OAuthResponseStrategy {
    @Override
    public String getProviderName() {
        return "kakao";
    }

    @Override
    public OAuthUserInfo createOAuthUserInfo(Map<String, Object> attributes) {
        return new KakaoUserInfo(attributes);
    }
}
