package com.adson.staymanager.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.adson.staymanager.controller.v1.MeController;
import com.adson.staymanager.entity.GuestProfile;
import com.adson.staymanager.entity.Role;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.repository.BookingRepository;
import com.adson.staymanager.repository.GuestProfileRepository;
import com.adson.staymanager.repository.UserRepository;

@WebMvcTest(MeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private GuestProfileRepository guestProfileRepository;

    @MockBean
    private BookingRepository bookingRepository;

    @Test
    void shouldReturnMeSuccessfully() throws Exception {
      
        // arranje

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("123456");
        user.setRole(Role.RECEPCAO);
        user.setActive(true);

        GuestProfile guest = new GuestProfile();
        ReflectionTestUtils.setField(guest, "id", 1L);
        guest.setUser(user);
        guest.setCpf("12345678900");
        guest.setPhone("11999999999");
        guest.setBirthDate(java.time.LocalDate.of(1990, 1, 1));
        guest.setCep("12345678");
        guest.setStreet("Main Street");
        guest.setNumber(123);
        guest.setDistrict("Downtown");
        guest.setCity("City");
        guest.setState("State");

        // act
         when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
         when(guestProfileRepository.findByUserId(user.getId())).thenReturn(Optional.of(guest));

         Authentication auth =
            new UsernamePasswordAuthenticationToken("john.doe@example.com", null);

         // assert
         mvc.perform(get("/api/v1/me")
                .principal(auth)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.userId").value(1L))
               .andExpect(jsonPath("$.name").value("John Doe"))
               .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }
    
}
