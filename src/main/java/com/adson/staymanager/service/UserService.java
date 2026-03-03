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

    public User create(User user, Role role) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new RuntimeException("Email já cadastrado");
    }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role effectiveRole = (role == null) ? Role.CLIENTE : role;
        user.setRole(effectiveRole);

        return userRepository.save(user);
    }
}