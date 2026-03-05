package com.adson.staymanager.service;

import com.adson.staymanager.dto.request.LoginRequestDTO;
import com.adson.staymanager.entity.Role;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.exception.InvalidCredentialsException;
import com.adson.staymanager.exception.UserNotFoundException;
import com.adson.staymanager.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @AfterEach
    void tearDown() {
    clearInvocations(userRepository, passwordEncoder);
    }

    @Test
    void shouldAuthenticateUser() {

        // arrange
        LoginRequestDTO request = new LoginRequestDTO();

        ReflectionTestUtils.setField(request, "email", "adson@email.com");
        ReflectionTestUtils.setField(request, "password", "123456");

        User user = new User();
        user.setEmail("adson@email.com");
        user.setName("Adson");
        user.setRole(Role.GERENCIA);
        user.setPassword("$2a$10$hashFake");

        when(userRepository.findByEmail("adson@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", user.getPassword())).thenReturn(true);

        // act
        User result = authService.authenticate(request);

        // assert
        assertNotNull(result);
        assertEquals("adson@email.com", result.getEmail());
        assertEquals(Role.GERENCIA, result.getRole());

        verify(userRepository).findByEmail("adson@email.com");
        verify(passwordEncoder).matches("123456", user.getPassword());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        
        LoginRequestDTO request = new LoginRequestDTO();

        ReflectionTestUtils.setField(request, "email", "naoexiste@email.com");
        ReflectionTestUtils.setField(request, "password", "123456");

        when(userRepository.findByEmail(eq("naoexiste@email.com"))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.authenticate(request));

        verify(userRepository).findByEmail("naoexiste@email.com");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowWhenPasswordInvalid() {
        
        LoginRequestDTO request = new LoginRequestDTO();

        ReflectionTestUtils.setField(request, "email", "adson@email.com");
        ReflectionTestUtils.setField(request, "password", "errada");

        User user = new User();
        user.setEmail("adson@email.com");
        user.setPassword("$2a$10$hashFake");

        when(userRepository.findByEmail(eq("adson@email.com"))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("errada", user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.authenticate(request));

        verify(userRepository).findByEmail("adson@email.com");
        verify(passwordEncoder).matches("errada", user.getPassword());
    }
}