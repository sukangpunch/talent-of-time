package com.example.talentoftime.auth.dto.oauth;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return ((Map)attributes.get("kakao_account")).get("email").toString();
    }

    @Override
    public String getName() {
        return ((Map)attributes.get("properties")).get("nickname").toString();
    }
}
