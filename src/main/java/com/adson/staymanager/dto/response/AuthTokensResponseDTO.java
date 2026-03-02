package com.adson.staymanager.dto.response;

public record AuthTokensResponseDTO(String accessToken, 
                                    String refreshToken, 
                                    String tokenType) {
    
}
