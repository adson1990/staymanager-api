package com.adson.staymanager.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.adson.staymanager.dto.request.LoginRequestDTO;
import com.adson.staymanager.repository.UserRepository;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.exception.InvalidCredentialsException;
import com.adson.staymanager.exception.UserNotFoundException;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Senha inválida");
        }

        return user;
    }
}
