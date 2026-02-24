package com.example.talentoftime.auth.service.oauth;

import java.util.Map;
import org.springframework.util.MultiValueMap;

public interface OAuthClient {

    Map<String, Object> getAccessTokenResponse(
            String tokenUri,
            MultiValueMap<String, String> tokenRequest
    );

    Map<String, Object> getUserInfoWithAccessToken(
            String uri,
            String accessToken
    );
}
