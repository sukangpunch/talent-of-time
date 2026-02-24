package com.example.talentoftime.auth.service.oauth;

import com.example.talentoftime.auth.dto.oauth.OAuthUserInfo;
import com.example.talentoftime.auth.strategy.OAuthResponseFactory;
import com.example.talentoftime.auth.util.RedirectHostConst;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthUserInfoService {
    private final static String GRANT_TYPE = "authorization_code";

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuthResponseFactory oAuthResponseFactory;
    private final OAuthClient oAuthClient;

    static final String PROVIDER_NAME = "kakao";

    public OAuthUserInfo getUserInfo(
            String authCode,
            String dest
    ) {
        validateAuthCode(authCode);
        String baseHost = validateDest(dest);

        String validProvider = PROVIDER_NAME;

        // 웹에서 특수 문자나 공백 등이 URL 인코딩 된 상태로 전달되는 문제를 해결하기 위함
        String decodedCode = URLDecoder.decode(authCode, StandardCharsets.UTF_8);

        // OAuth 설정 정보 가져오기
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(validProvider);

        // 토큰 요청
        String redirectUri = baseHost + "/login/oauth2/code/" + validProvider;
        String accessToken = getAccessToken(registration, decodedCode, redirectUri);

        // 사용자 정보 요청
        Map<String, Object> userInfo = getUserInfo(registration, accessToken);

        return oAuthResponseFactory.createOAuthUserInfo(validProvider, userInfo);
    }

    private Map<String, Object> getUserInfo(
            ClientRegistration registration,
            String accessToken
    ) {

        // 사용자 정보를 조회하기 위한 엔드포인트
        String userInfoUri = registration.getProviderDetails().getUserInfoEndpoint().getUri();

        return oAuthClient.getUserInfoWithAccessToken(userInfoUri, accessToken);
    }

    private String getAccessToken(
            ClientRegistration registration,
            String decodedCode,
            String redirectUri
    ) {

        // 요청 만들기
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("grant_type", GRANT_TYPE);
        tokenRequest.add("client_id", registration.getClientId());
        tokenRequest.add("client_secret", registration.getClientSecret());
        tokenRequest.add("redirect_uri", redirectUri);
        tokenRequest.add("code", decodedCode);

        // AccessToken 을 발급받기 위한 엔드포인트
        String tokenUri = registration.getProviderDetails().getTokenUri();

        Map<String, Object> tokenResponse = oAuthClient.getAccessTokenResponse(tokenUri, tokenRequest);
        return (String) tokenResponse.get("access_token");
    }

    private String validateDest(String dest) {
        String base = RedirectHostConst.DEST_BASE.get(dest);

        if(base == null || base.isBlank()){
            throw new BusinessException(ErrorCode.DEST_NOT_VALID);
        }

        return base;
    }

    private void validateAuthCode(String authCode) {
        if(authCode == null || authCode.isBlank()){
            throw new BusinessException(ErrorCode.AUTH_CODE_INVALID);
        }
    }
}

