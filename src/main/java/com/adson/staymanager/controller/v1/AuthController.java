package com.adson.staymanager.controller.v1;

import com.adson.staymanager.dto.request.LoginRequestDTO;
import com.adson.staymanager.dto.request.RefreshTokenRequestDTO;
import com.adson.staymanager.entity.User;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.adson.staymanager.dto.response.AuthTokensResponseDTO;
import com.adson.staymanager.service.AuthService;
import com.adson.staymanager.service.RefreshTokenService;
import com.adson.staymanager.security.TokenService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokensResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        User user = authService.authenticate(request);

        String access = tokenService.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refresh = refreshTokenService.issueRefreshToken(user);

        return ResponseEntity.ok(new AuthTokensResponseDTO(access, refresh, "Bearer"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthTokensResponseDTO> refresh(
        @Valid @RequestBody RefreshTokenRequestDTO request) {

       User user = refreshTokenService.validateAndRotate(request.refreshToken());

       String newAccess = tokenService.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
       String newRefresh = refreshTokenService.issueRefreshToken(user);

    return ResponseEntity.ok(new AuthTokensResponseDTO(newAccess, newRefresh, "Bearer"));
    }
}