package com.adson.staymanager.service;

import com.adson.staymanager.dto.request.LoginRequestDTO;
import com.adson.staymanager.entity.Role;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.exception.InvalidCredentialsException;
import com.adson.staymanager.exception.UserNotFoundException;
import com.adson.staymanager.repository.LoginAuditRepository;
import com.adson.staymanager.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    //@Mock private PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    LoginAuditRepository loginAuditRepository;

    @InjectMocks
    private AuthService authService;

    @AfterEach
    void tearDown() {
    clearInvocations(userRepository, authenticationManager, loginAuditRepository);
    }

    @Test
    void shouldAuthenticateUser() {

        // arrange
        LoginRequestDTO request = new LoginRequestDTO();
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        ReflectionTestUtils.setField(request, "email", "adson@email.com");
        ReflectionTestUtils.setField(request, "password", "123456");

        User user = new User();
        user.setEmail("adson@email.com");
        user.setName("Adson");
        user.setRole(Role.GERENCIA);
        user.setPassword("$2a$10$hashFake");

        when(userRepository.findByEmail("adson@email.com")).thenReturn(Optional.of(user));
        //when(passwordEncoder.matches("123456", user.getPassword())).thenReturn(true);
        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpRequest.getHeader("User-Agent")).thenReturn("JUnit Test");

        // act
        User result = authService.authenticate(request, httpRequest);

        // assert
        assertNotNull(result);
        assertEquals("adson@email.com", result.getEmail());
        assertEquals(Role.GERENCIA, result.getRole());

        verify(userRepository).findByEmail("adson@email.com");
        //verify(passwordEncoder).matches("123456", user.getPassword());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        
        LoginRequestDTO request = new LoginRequestDTO();
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        ReflectionTestUtils.setField(request, "email", "naoexiste@email.com");
        ReflectionTestUtils.setField(request, "password", "123456");

        when(userRepository.findByEmail(eq("naoexiste@email.com"))).thenReturn(Optional.empty());
       // when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
       // when(httpRequest.getHeader("User-Agent")).thenReturn("JUnit Test");

        assertThrows(UserNotFoundException.class, () -> authService.authenticate(request, httpRequest));

        verify(userRepository).findByEmail("naoexiste@email.com");
    }

    @Test
    void shouldThrowWhenPasswordInvalid() {
        
        LoginRequestDTO request = new LoginRequestDTO();
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        ReflectionTestUtils.setField(request, "email", "adson@email.com");
        ReflectionTestUtils.setField(request, "password", "errada");

        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpRequest.getHeader("User-Agent")).thenReturn("JUnit Test");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Senha incorreta"));

        assertThrows(InvalidCredentialsException.class, () -> authService.authenticate(request,httpRequest));

        verify(authenticationManager).authenticate(argThat(auth -> 
            auth.getPrincipal().equals("adson@email.com") && 
            auth.getCredentials().equals("errada")
        ));
    }
}