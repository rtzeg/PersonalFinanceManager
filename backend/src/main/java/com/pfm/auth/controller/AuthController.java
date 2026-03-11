package com.pfm.auth.controller;

import com.pfm.auth.dto.AuthRequest;
import com.pfm.auth.dto.AuthResponse;
import com.pfm.auth.service.AuthService;
import com.pfm.common.util.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CurrentUserService currentUserService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid AuthRequest request) { return authService.register(request); }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid AuthRequest request, HttpServletRequest servletRequest) {
        AuthResponse response = authService.login(request);
        servletRequest.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                org.springframework.security.core.context.SecurityContextHolder.getContext());
        return response;
    }

    @GetMapping("/me")
    public AuthResponse me() { return authService.me(currentUserService.getUserId()); }
}
