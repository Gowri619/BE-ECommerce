package com.gowri.commerceflow.controller;

import com.gowri.commerceflow.dto.request.LoginRequest;
import com.gowri.commerceflow.dto.response.LoginResponse;
import com.gowri.commerceflow.dto.request.RegisterRequest;
import com.gowri.commerceflow.dto.response.ProductResponse;
import com.gowri.commerceflow.dto.response.RegisterResponse;
import com.gowri.commerceflow.service.AuthService;
import com.gowri.commerceflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
