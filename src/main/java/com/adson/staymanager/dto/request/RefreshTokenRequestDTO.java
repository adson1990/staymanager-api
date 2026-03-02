package com.adson.staymanager.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(@NotBlank(message = "refreshToken é obrigatório")
                                     String refreshToken) {
    
}
