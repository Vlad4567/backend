package com.example.beautybook.controller;

import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserLoginRequestDto;
import com.example.beautybook.dto.user.UserLoginResponseDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.security.AuthenticationService;
import com.example.beautybook.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/registration")
    public UserDto register(@RequestBody @Valid UserRegistrationDto requestDto) {
        return userService.createUser(requestDto);
    }

    @PostMapping("/auth/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authentication(request);
    }
}
