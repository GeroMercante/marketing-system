package com.davinci.geromercante.marketing.common.util;

import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import com.davinci.geromercante.marketing.module.auth.model.enums.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.expiration.time}")
    private long expirationTime;

    @Value("${jwt.secret}")
    private String secretString;

    private SecretKey secretKey;
    private final MessageUtil messageUtil;

    private static final String USER_KEY_ID = "userId";
    private static final String CLIENT_KEY_ID = "sellerId";
    private static final String PROFILE_KEY = "profile";
    private static final String ROLE_KEY = "role";

    public JwtUtil(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @PostConstruct
    public void init() {
        if (secretString != null) {
            byte[] keyBytes = Base64.getDecoder().decode(secretString);
            this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        }
    }

    public String generateToken(Credential credential) {
        return Jwts.builder()
                .subject(credential.getUser().getEmail())
                .claim(USER_KEY_ID, credential.getUser().getId())
                .claim(PROFILE_KEY, credential.getUser().getProfile() != null ? credential.getUser().getProfile().getName() : null)
                .claim(ROLE_KEY, credential.getUser().getRole() != null ? credential.getUser().getRole() : null)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public String changeToken(Credential credential, Long clientId) {
        return Jwts.builder()
                .subject(credential.getUser().getEmail())
                .claim(USER_KEY_ID, credential.getUser().getId())
                .claim(PROFILE_KEY, credential.getUser().getProfile() != null ? credential.getUser().getProfile().getName() : null)
                .claim(ROLE_KEY, credential.getUser().getRole() != null ? credential.getUser().getRole() : null)
                .claim(CLIENT_KEY_ID, clientId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public Long getUserIdFromToken(String authHeader) {
        final String token = getToken(authHeader);
        return extractClaims(token, claims -> claims.get(USER_KEY_ID, Long.class));
    }

    public Long getPasswordRecoveryId(String authHeader) {
        final String token = getToken(authHeader);
        return extractClaims(token, claims -> claims.get("passwordRecoveryId", Long.class));
    }

    public boolean isTokenValid(String authHeader, UserDetails userDetails) {
        final String username = extractUsername(authHeader);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(authHeader));
    }

    public Long getClientIdFromToken(String authHeader) {
        final String token = getToken(authHeader);
        return extractClaims(token, claims -> claims.get(CLIENT_KEY_ID, Long.class));
    }

    public Long getClientIdFromTokenStrict(String authHeader) throws MarketingException {
        Long clientId = getClientIdFromToken(authHeader);
        if (clientId == null) {
            throw new MarketingException(messageUtil.getMessage("error.invalid-client-id"));
        }
        return clientId;
    }

    public RoleEnum getRoleEnum(String authHeader) {
        final String token = getToken(authHeader);
        String profile = extractClaims(token, claims -> claims.get(ROLE_KEY, String.class));
        return profile != null ? RoleEnum.valueOf(profile) : null;
    }

    public String extractUsername(String authHeader) {
        final String token = getToken(authHeader);
        return extractClaims(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String authHeader) {
        final String token = getToken(authHeader);
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public String getToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else {
            return authHeader;
        }
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsFunction) {
        return claimsFunction.apply(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload());
    }
}
