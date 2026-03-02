package com.adson.staymanager.dto.response;

public record LoginResponseDTO(
    Long id,
    String name,
    String email,
    String role
) {}
