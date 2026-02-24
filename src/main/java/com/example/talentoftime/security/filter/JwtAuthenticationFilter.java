package com.example.talentoftime.security.filter;

import com.example.talentoftime.auth.service.AuthTokenProvider;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.security.exception.CustomAuthenticationEntryPoint;
import com.example.talentoftime.security.exception.CustomAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<HttpEndpoint> EXCLUDE_ENDPOINTS = List.of(
            /* swagger */
            HttpEndpoint.prefix("/swagger-ui", HttpMethod.GET),
            HttpEndpoint.prefix("/v3/api-docs", HttpMethod.GET),

            /* auth */
            HttpEndpoint.prefix("/api/auth", HttpMethod.POST),

            /* oauth */
            HttpEndpoint.prefix("/api/v1/oauth", HttpMethod.GET)
    );

    private final AuthTokenProvider authTokenProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = request.getHeader("Authorization");

            if (checkTokenNotNullAndBearer(token)) {
                String jwtToken = token.substring(7);

                authTokenProvider.validateToken(jwtToken);
                Authentication authentication = authTokenProvider.getAuthUser(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                Long crewId = authTokenProvider.parseUser(jwtToken).getId();
                request.setAttribute("crew_id", crewId);

                log.info("[JwtAuthenticationFilter] 토큰 검증 완료. crewId: {}", crewId);
            }

            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            authenticationEntryPoint.commence(
                    request, response, new CustomAuthenticationException(e.getErrorCode()));
        }
    }

    private boolean checkTokenNotNullAndBearer(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return EXCLUDE_ENDPOINTS.stream()
                .anyMatch(endpoint -> endpoint.isMatchedWith(requestURI, method));
    }

    private enum MatchType {
        EXACT,
        PREFIX
    }

    private record HttpEndpoint(
            String pattern,
            List<HttpMethod> methods,
            MatchType type
    ) {
        static HttpEndpoint exact(String path, HttpMethod... methods) {
            return new HttpEndpoint(path, List.of(methods), MatchType.EXACT);
        }

        static HttpEndpoint prefix(String prefix, HttpMethod... methods) {
            return new HttpEndpoint(prefix, List.of(methods), MatchType.PREFIX);
        }

        boolean isMatchedWith(String uri, String method) {
            if (methods.stream().noneMatch(m -> m.name().equalsIgnoreCase(method))) {
                return false;
            }
            return switch (type) {
                case EXACT -> uri.equals(pattern);
                case PREFIX -> uri.startsWith(pattern);
            };
        }
    }
}
