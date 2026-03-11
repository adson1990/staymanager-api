package com.adson.staymanager.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;


import com.adson.staymanager.dto.request.LoginRequestDTO;
import com.adson.staymanager.repository.LoginAuditRepository;
import com.adson.staymanager.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import com.adson.staymanager.entity.LoginAudit;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.exception.InvalidCredentialsException;
import com.adson.staymanager.exception.UserNotFoundException;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final LoginAuditRepository loginAuditRepository;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       LoginAuditRepository loginAuditRepository,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.loginAuditRepository = loginAuditRepository;
        this.authenticationManager = authenticationManager;
    }

    public User authenticate(LoginRequestDTO request, HttpServletRequest httpRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado"));

            LoginAudit audit = new LoginAudit();
            audit.setUserId(user.getId());
            audit.setEmail(user.getEmail());
            audit.setIpAddress(httpRequest.getRemoteAddr());
            audit.setUserAgent(httpRequest.getHeader("User-Agent"));
            audit.setOutcome(LoginAudit.LoginOutcome.SUCCESS);
            audit.setTimestamp(LocalDateTime.now());

            loginAuditRepository.save(audit);

            return user;

        } catch (AuthenticationException ex) {

        LoginAudit audit = new LoginAudit();
        audit.setEmail(request.getEmail());
        audit.setIpAddress(httpRequest.getRemoteAddr());
        audit.setUserAgent(httpRequest.getHeader("User-Agent"));
        audit.setOutcome(LoginAudit.LoginOutcome.FAILURE);
        audit.setFailureReason(ex.getMessage());
        audit.setTimestamp(LocalDateTime.now());

        loginAuditRepository.save(audit);

        throw new InvalidCredentialsException("E-mail ou senha inválidos");
        }
    }
}
