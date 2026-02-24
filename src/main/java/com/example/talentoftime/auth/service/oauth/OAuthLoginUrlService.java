package com.example.talentoftime.auth.service.oauth;

import com.example.talentoftime.auth.util.RedirectHostConst;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class OAuthLoginUrlService {
    private final ClientRegistrationRepository clientRegistrationRepository;

    static final String PROVIDER_NAME = "kakao";

    public String generateLoginUrl(
            String dest
    ) {
        String baseHost = validateDest(dest);
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(PROVIDER_NAME);
        String authorizationUri = registration.getProviderDetails().getAuthorizationUri();
        String clientId = registration.getClientId();
        String responseType = "code";
        String redirectUri = baseHost + "/login/oauth2/code/" + PROVIDER_NAME;
        String scope = String.join(" ", registration.getScopes()); // 공백이 표준

        return UriComponentsBuilder.fromUriString(authorizationUri)
                .queryParam("client_id", clientId)
                .queryParam("response_type", responseType)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope",scope)
                .build().toUriString();
    }

    private String validateDest(String dest) {
        String base = RedirectHostConst.DEST_BASE.get(dest);

        if(base == null || base.isBlank()){
            throw new BusinessException(ErrorCode.DEST_NOT_VALID);
        }

        return base;
    }
}
