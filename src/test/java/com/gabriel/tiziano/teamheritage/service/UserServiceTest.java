package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.UserRequest;
import com.gabriel.tiziano.teamheritage.dto.response.UserResponse;
import com.gabriel.tiziano.teamheritage.entities.Scope;
import com.gabriel.tiziano.teamheritage.entities.User;
import com.gabriel.tiziano.teamheritage.exception.EmailAlreadyExistsException;
import com.gabriel.tiziano.teamheritage.mapper.UserMapper;
import com.gabriel.tiziano.teamheritage.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock ScopeService scopeService;
    @Mock UserMapper userMapper;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks UserService userService;

    @Test
    void createUser_shouldEncodePasswordAndSave() {
        UserRequest request = new UserRequest("Gabriel", "gabriel@teamheritage.com", "senha12345", List.of(1L));
        Scope scope = Scope.builder().id(1L).name("club:read").build();
        User mapped = User.builder().name("Gabriel").email("gabriel@teamheritage.com").build();
        User saved = User.builder().id(1L).name("Gabriel").email("gabriel@teamheritage.com").active(true).build();
        UserResponse response = new UserResponse(1L, "Gabriel", "gabriel@teamheritage.com", true, List.of("club:read"));

        when(userRepository.findByEmail("gabriel@teamheritage.com")).thenReturn(Optional.empty());
        when(scopeService.findById(1L)).thenReturn(scope);
        when(userMapper.toEntity(request)).thenReturn(mapped);
        when(passwordEncoder.encode("senha12345")).thenReturn("$2b$10$hash");
        when(userRepository.save(mapped)).thenReturn(saved);
        when(userMapper.toResponse(saved)).thenReturn(response);

        UserResponse result = userService.createUser(request);

        assertEquals(1L, result.id());
        assertTrue(mapped.isActive());
        assertEquals("$2b$10$hash", mapped.getPassword());
        verify(passwordEncoder).encode("senha12345");
        verify(userRepository).save(mapped);
    }

    @Test
    void createUser_whenEmailExists_shouldThrowException() {
        UserRequest request = new UserRequest("Gabriel", "gabriel@teamheritage.com", "senha12345", List.of(1L));

        when(userRepository.findByEmail("gabriel@teamheritage.com")).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any());
    }
}