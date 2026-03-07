package com.adson.staymanager.controller;

import com.adson.staymanager.controller.v1.AuthController;
import com.adson.staymanager.dto.request.LoginRequestDTO;
import com.adson.staymanager.entity.Role;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.service.AuthService;
import com.adson.staymanager.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;

import com.adson.staymanager.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenService tokenService;

     @MockBean
    private RefreshTokenService refreshTokenService;

    @SuppressWarnings("null")
    @Test
    void shouldLoginSuccessfully() throws Exception {
        // arranje
        User user = new User();
        user.setName("Carlos");
        user.setEmail("carlos@email.com");
        user.setRole(Role.CLIENTE);

        ReflectionTestUtils.setField(user, "id", 1L);

        // act
        when(authService.authenticate(any(LoginRequestDTO.class), any(HttpServletRequest.class))).thenReturn(user);
        when(tokenService.generateAccessToken(any(Long.class), any(String.class), any(Role.class)))
        .thenReturn("token123");
        when(refreshTokenService.issueRefreshToken(any(User.class))).thenReturn("refresh123");

        //assert
        mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"carlos@email.com","password":"123456"}
                        """))
                .andExpect(status().isOk())
                // Validando os campos JSON da resposta
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
}
