package com.example.talentoftime.auth.token;

import static io.jsonwebtoken.Jwts.SIG.HS256;

import com.example.talentoftime.auth.domain.LoginUser;
import com.example.talentoftime.auth.domain.Subject;
import com.example.talentoftime.auth.token.config.JwtProperties;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.crew.domain.Crew;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    public JwtProvider(JwtProperties jwtProperties) {
        this.secretKey = new SecretKeySpec(jwtProperties.secret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public String generateToken(
            Subject subject,
            Map<String, String> claims,
            Duration expireTime
    ) {
        ZonedDateTime now = ZonedDateTime.now(SEOUL_ZONE);
        ZonedDateTime expiredDateTime = now.plus(expireTime);

        Date nowDate = Date.from(now.toInstant());
        Date expiredDate = Date.from(expiredDateTime.toInstant());

        return Jwts.builder()
                .subject(subject.value())
                .claims(claims)
                .issuedAt(nowDate)
                .expiration(expiredDate)
                .signWith(secretKey, HS256)
                .compact();
    }

    public String generateToken(
            Subject subject,
            Duration expireTime
    ) {
        ZonedDateTime now = ZonedDateTime.now(SEOUL_ZONE);
        ZonedDateTime expiredDateTime = now.plus(expireTime);

        Date nowDate = Date.from(now.toInstant());
        Date expiredDate = Date.from(expiredDateTime.toInstant());
        return Jwts.builder()
                .subject(subject.value())
                .issuedAt(nowDate)
                .expiration(expiredDate)
                .signWith(secretKey, HS256)
                .compact();
    }

    public Subject parseSubject(String token) {
        String subject = getClaims(token).getSubject();
        return new Subject(subject);
    }

    public Authentication getAuthentication(Crew crew) {
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + (crew.getRole() != null ? crew.getRole().name() : "USER")));

        LoginUser loginUser = new LoginUser(crew.getId(), crew.getProviderId(), null, authorities);

        return new UsernamePasswordAuthenticationToken(loginUser, "", loginUser.getAuthorities());
    }

    public Long getCrewId(String token) {
        String subject = getClaims(token).getSubject();
        return Long.parseLong(subject);
    }

    public Claims getClaims(String token) {
        return handleJwtException(token, (value) ->
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(value).getPayload()
        );
    }

    public boolean validateToken(String token) {
        try {
            handleJwtException(token, (value) -> {
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(value);
                return null;
            });
            return true;
        } catch (BusinessException e) {
            return false;
        }
    }

    private <T> T handleJwtException(
            String token,
            Function<String, T> function
    ) {
        try {
            return function.apply(token);
        } catch (MalformedJwtException malformedJwtException) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new BusinessException(ErrorCode.TOKEN_EMPTY);
        } catch (SignatureException signatureException) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_SIGNED);
        } catch (JwtException jwtException) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }
    }
}
