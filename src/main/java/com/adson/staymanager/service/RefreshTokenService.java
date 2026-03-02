package com.adson.staymanager.service;

import com.adson.staymanager.entity.RefreshToken;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTtlDays;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${security.jwt.refresh-token-ttl-days}") long refreshTtlDays
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTtlDays = refreshTtlDays;
    }

    public String issueRefreshToken(User user) {
        String raw = UUID.randomUUID().toString(); // token opaco
        String hash = sha256(raw);

        Instant exp = Instant.now().plus(refreshTtlDays, ChronoUnit.DAYS);
        RefreshToken rt = new RefreshToken(hash, user, exp);

        refreshTokenRepository.save(rt);
        return raw;
    }

    public User validateAndRotate(String rawToken) {
        String hash = sha256(rawToken);

        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (stored.isRevoked() || stored.isExpired()) {
            throw new RuntimeException("Refresh token expirado ou revogado");
        }

        stored.revoke();
        refreshTokenRepository.save(stored);

        return stored.getUser();
    }

    private String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar hash", e);
        }
    }
}