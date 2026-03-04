package com.adson.staymanager.dto.response;

import com.adson.staymanager.entity.GuestProfile;
import com.adson.staymanager.entity.User;

import java.time.LocalDate;

public record MeResponseDTO(
        Long userId,
        String name,
        String email,
        String role,
        Long guestId,
        String cpf,
        String phone,
        LocalDate birthDate,
        String cep,
        String street,
        int number,
        String district,
        String city,
        String state
) {
    public static MeResponseDTO from(User user, GuestProfile profile) {
        return new MeResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                profile.getId(),
                profile.getCpf(),
                profile.getPhone(),
                profile.getBirthDate(),
                profile.getCep(),
                profile.getStreet(),
                profile.getNumber(),
                profile.getDistrict(),
                profile.getCity(),
                profile.getState()
        );
    }
}