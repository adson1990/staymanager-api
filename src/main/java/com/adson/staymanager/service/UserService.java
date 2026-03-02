package com.adson.staymanager.service;

import com.adson.staymanager.entity.Role;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user, int roleID) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new RuntimeException("Email já cadastrado");
    }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        switch (roleID) {
            case 1:
                user.setRole(Role.ADMIN);
            case 2:
                user.setRole(Role.RECEPCAO);
            case 3:
                user.setRole(Role.MANUTENCAO);
            case 4:
                user.setRole(Role.GERENCIA);
            case 5:
                user.setRole(Role.CLIENTE);    
                break;
        
            default: user.setRole(Role.RECEPCAO);
                break;
        }

        return userRepository.save(user);
    }
}