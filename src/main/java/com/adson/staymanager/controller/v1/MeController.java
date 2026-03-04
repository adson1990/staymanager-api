package com.adson.staymanager.controller.v1;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adson.staymanager.entity.GuestProfile;
import com.adson.staymanager.exception.BusinessRuleException;
import com.adson.staymanager.repository.GuestProfileRepository;
import com.adson.staymanager.repository.UserRepository;
import com.adson.staymanager.dto.response.MeResponseDTO;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final UserRepository userRepository;
    private final GuestProfileRepository guestProfileRepository;

    public MeController(UserRepository userRepository, GuestProfileRepository guestProfileRepository) {
        this.userRepository = userRepository;
        this.guestProfileRepository = guestProfileRepository;
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping
    public MeResponseDTO me(Authentication authentication) {

        String email = authentication.getName(); // principal
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));

        GuestProfile profile = guestProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessRuleException("Perfil de hóspede não encontrado"));

        return MeResponseDTO.from(user, profile);
    }
}