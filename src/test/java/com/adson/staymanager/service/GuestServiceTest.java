package com.adson.staymanager.service;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.adson.staymanager.dto.request.GuestCreateRequestDTO;
import com.adson.staymanager.entity.GuestProfile;
import com.adson.staymanager.entity.Role;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.repository.GuestProfileRepository;
import com.adson.staymanager.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class GuestServiceTest {
    
    @Mock GuestProfileRepository guestRepository;
    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        // Limpa as interações com os mocks após cada teste
        // para evitar interferência entre os testes
        // e garantir que cada teste seja independente.
        clearInvocations(guestRepository, userRepository, passwordEncoder);
    }

    @InjectMocks GuestService guestService;

    @Test
    void shouldCreateGuestSuccessfully() {
        GuestCreateRequestDTO dto = new GuestCreateRequestDTO(
                "John Doe",
                "john@email.com",
                "123456",
                "12345678900",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                "12345678",
                "Main Street",
                123,
                "Downtown",
                "City",
                "State"
        );

        User savedUser = new User();
        ReflectionTestUtils.setField(savedUser, "id", 1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("john@email.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.CLIENTE);

        GuestProfile savedGuest = new GuestProfile();
        ReflectionTestUtils.setField(savedGuest, "id", 1L);
        savedGuest.setUser(savedUser);
        savedGuest.setCpf("12345678900");
        savedGuest.setPhone("11999999999");
        savedGuest.setBirthDate(LocalDate.of(1990, 1, 1));
        savedGuest.setCep("12345678");
        savedGuest.setStreet("Main Street");
        savedGuest.setNumber(123);
        savedGuest.setDistrict("Downtown");
        savedGuest.setCity("City");
        savedGuest.setState("State");

        when(guestRepository.existsByCpf("12345678900")).thenReturn(false);
        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(guestRepository.save(any(GuestProfile.class))).thenReturn(savedGuest);

        GuestProfile result = guestService.createGuest(dto);

        assertNotNull(result);
        assertEquals("12345678900", result.getCpf());
        assertEquals("john@email.com", result.getUser().getEmail());
        assertEquals(Role.CLIENTE, result.getUser().getRole());

        verify(guestRepository).existsByCpf("12345678900");
        verify(userRepository).findByEmail("john@email.com");
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(User.class));
        verify(guestRepository).save(any(GuestProfile.class));
    }
}
