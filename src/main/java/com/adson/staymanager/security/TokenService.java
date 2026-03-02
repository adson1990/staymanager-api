package com.adson.staymanager.security;

import com.adson.staymanager.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;

@Service
public class TokenService {

    private final SecretKey key;

    private final String issuer;
    private final long accessTtlMinutes;

    public TokenService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.access-token-ttl-minutes}") long accessTtlMinutes
    ) {
        this.issuer = issuer;
        this.accessTtlMinutes = accessTtlMinutes;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long userId, String email, Role role) {
        Instant now = Instant.now();
        Instant exp = now.plus(accessTtlMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .issuer(issuer)
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim("uid", userId)
                .claim("role", role.name())
                .signWith(key)
                .compact();
    }

    public SecretKey getKey() {
        return key;
    }

    public String getIssuer() {
        return issuer;
    }
}