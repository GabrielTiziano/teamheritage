package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.entities.Scope;
import com.gabriel.tiziano.teamheritage.exception.ResourceNotFoundException;
import com.gabriel.tiziano.teamheritage.repository.ScopeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScopeServiceTest {

    @Mock ScopeRepository scopeRepository;

    @InjectMocks ScopeService scopeService;

    @Test
    void findById_whenExists_shouldReturnScope() {
        Scope scope = Scope.builder().id(1L).name("club:read").build();
        when(scopeRepository.findById(1L)).thenReturn(Optional.of(scope));

        Scope result = scopeService.findById(1L);

        assertEquals("club:read", result.getName());
    }

    @Test
    void findById_whenNotFound_shouldThrowException() {
        when(scopeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> scopeService.findById(999L));
    }
}