package com.adson.staymanager.service;

import com.adson.staymanager.dto.request.GuestCreateRequestDTO;
import com.adson.staymanager.entity.GuestProfile;
import com.adson.staymanager.entity.Role;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.exception.BusinessRuleException;
import com.adson.staymanager.repository.GuestProfileRepository;
import com.adson.staymanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestService {

    private final GuestProfileRepository guestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public GuestService(
            GuestProfileRepository guestRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.guestRepository = guestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public GuestProfile createGuest(GuestCreateRequestDTO dto) {

        if (guestRepository.existsByCpf(dto.cpf())) {
            throw new BusinessRuleException("CPF já cadastrado");
        }

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new BusinessRuleException("Email já cadastrado");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.CLIENTE);

        User savedUser = userRepository.save(user);

        GuestProfile profile = new GuestProfile();
        profile.setUser(savedUser);
        profile.setCpf(dto.cpf());
        profile.setPhone(dto.phone());
        profile.setBirthDate(dto.birthDate());
        profile.setCep(dto.cep());
        profile.setStreet(dto.street());
        profile.setNumber(dto.number());
        profile.setDistrict(dto.district());
        profile.setCity(dto.city());
        profile.setState(dto.state());

        return guestRepository.save(profile);
    }
}