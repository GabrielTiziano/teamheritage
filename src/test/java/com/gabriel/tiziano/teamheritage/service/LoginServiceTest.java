package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.LoginRequest;
import com.gabriel.tiziano.teamheritage.dto.response.LoginResponse;
import com.gabriel.tiziano.teamheritage.entities.User;
import com.gabriel.tiziano.teamheritage.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtEncoder jwtEncoder;

    @InjectMocks LoginService loginService;

    @Test
    void login_withValidCredentials_shouldReturnToken() {
        LoginRequest request = new LoginRequest("admin@teamheritage.com", "admin123");
        User user = User.builder().id(1L).email("admin@teamheritage.com").password("$2b$10$hash").build();
        Jwt jwt = mock(Jwt.class);

        when(userRepository.findByEmail("admin@teamheritage.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin123", "$2b$10$hash")).thenReturn(true);
        when(jwt.getTokenValue()).thenReturn("token123");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        LoginResponse result = loginService.login(request);

        assertEquals("token123", result.accessToken());
    }

    @Test
    void login_whenUserNotFound_shouldThrowBadCredentials() {
        LoginRequest request = new LoginRequest("naoexiste@teamheritage.com", "senha");

        when(userRepository.findByEmail("naoexiste@teamheritage.com")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> loginService.login(request));
        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    void login_whenPasswordIsWrong_shouldThrowBadCredentials() {
        LoginRequest request = new LoginRequest("admin@teamheritage.com", "senhaErrada");
        User user = User.builder().id(1L).email("admin@teamheritage.com").password("$2b$10$hash").build();

        when(userRepository.findByEmail("admin@teamheritage.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senhaErrada", "$2b$10$hash")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> loginService.login(request));
        verify(jwtEncoder, never()).encode(any());
    }
}