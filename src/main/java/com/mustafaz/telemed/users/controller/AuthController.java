package com.mustafaz.telemed.users.controller;

import com.mustafaz.telemed.res.Response;
import com.mustafaz.telemed.users.dto.LoginRequest;
import com.mustafaz.telemed.users.dto.LoginResponse;
import com.mustafaz.telemed.users.dto.RegistrationRequest;
import com.mustafaz.telemed.users.dto.ResetPasswordRequest;
import com.mustafaz.telemed.users.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<String>> register(@RequestBody @Valid RegistrationRequest registrationRequest){
        return ResponseEntity.ok(authService.register(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response<?>> forgotPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return ResponseEntity.ok(authService.forgetPassword(resetPasswordRequest.getEmail()));
    }
}