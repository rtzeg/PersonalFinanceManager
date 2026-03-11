package com.pfm.auth.service;

import com.pfm.auth.dto.AuthRequest;
import com.pfm.auth.dto.AuthResponse;
import com.pfm.common.exception.ApiException;
import com.pfm.user.entity.User;
import com.pfm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);
        return new AuthResponse(user.getId(), user.getUsername());
    }

    public AuthResponse login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userRepository.findByUsername(request.username()).orElseThrow();
        return new AuthResponse(user.getId(), user.getUsername());
    }

    public AuthResponse me(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return new AuthResponse(user.getId(), user.getUsername());
    }
}
