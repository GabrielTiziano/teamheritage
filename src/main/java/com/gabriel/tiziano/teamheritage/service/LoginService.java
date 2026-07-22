package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.LoginRequest;
import com.gabriel.tiziano.teamheritage.dto.response.LoginResponse;
import com.gabriel.tiziano.teamheritage.entities.Scope;
import com.gabriel.tiziano.teamheritage.entities.User;
import com.gabriel.tiziano.teamheritage.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.email());

        if (optionalUser.isEmpty() || !isPasswordCorrect(loginRequest.password(), optionalUser.get().getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = optionalUser.get();

        List<String> scopes = user.getScopes().stream().map(Scope::getName).toList();

        long expiresIn = 900L;

        JwtClaimsSet jwt = JwtClaimsSet.builder()
                .issuer("team-heritage")
                .subject(user.getEmail())
                .expiresAt(java.time.Instant.now().plusSeconds(expiresIn))
                .issuedAt(java.time.Instant.now())
                .claim("email", user.getEmail())
                .claim("scopes", scopes)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();

        return new LoginResponse(
                token,
                String.valueOf(expiresIn)
        );
    }

    private boolean isPasswordCorrect(String password, String savedPassword) {
        return passwordEncoder.matches(password, savedPassword);
    }
}
