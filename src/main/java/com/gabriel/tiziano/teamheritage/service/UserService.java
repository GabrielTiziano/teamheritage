package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.UserRequest;
import com.gabriel.tiziano.teamheritage.dto.response.UserResponse;
import com.gabriel.tiziano.teamheritage.entities.Scope;
import com.gabriel.tiziano.teamheritage.entities.User;
import com.gabriel.tiziano.teamheritage.mapper.UserMapper;
import com.gabriel.tiziano.teamheritage.repository.ScopeRepository;
import com.gabriel.tiziano.teamheritage.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ScopeService scopeService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ScopeRepository scopeRepository, ScopeService scopeService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.scopeService = scopeService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userAlreadyExists(userRequest.email())) {
            throw new IllegalArgumentException("User with email " + userRequest.email() + " already exists.");
        }

        List<Scope> scopes = userRequest.scopes().stream()
                .map(scopeService::findById)
                .toList();

        User newUser = userMapper.toEntity(userRequest);
        newUser.setActive(true);
        newUser.setScopes(scopes);
        newUser.setPassword(passwordEncoder.encode(userRequest.password()));
        return userMapper.toResponse(userRepository.save(newUser));

    }

    private boolean userAlreadyExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
