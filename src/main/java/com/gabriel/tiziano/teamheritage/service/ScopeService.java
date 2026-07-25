package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.entities.Scope;
import com.gabriel.tiziano.teamheritage.exception.ResourceNotFoundException;
import com.gabriel.tiziano.teamheritage.repository.ScopeRepository;
import org.springframework.stereotype.Service;

@Service
public class ScopeService {
    private final ScopeRepository scopeRepository;

    public ScopeService(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    public Scope findById(Long id) {
        return scopeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scope with id " + id + " not found."));
    }
}