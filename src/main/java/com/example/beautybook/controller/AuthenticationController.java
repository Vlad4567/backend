package com.example.beautybook.controller;

import com.example.beautybook.dto.user.RequestRefreshDto;
import com.example.beautybook.dto.user.ResponseRefreshDto;
import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserLoginRequestDto;
import com.example.beautybook.dto.user.UserLoginResponseDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.security.AuthenticationService;
import com.example.beautybook.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Content-Type", "Authorization"})
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

    @GetMapping("/auth/verificationMail/{token}")
    public String verificationMail(@PathVariable String token) {
        if (authenticationService.verificationEmail(token)) {
            return "ok";
        }
        return "not ok";
    }

    @PostMapping("/forgot-password/{email}")
    public String forgotPassword(@PathVariable @Email String email) {
        userService.forgotPassword(email);
        return "ok";
    }

    @GetMapping("/auth/checkEmail")
    public boolean checkEmailExistence(@RequestParam @Email @NotBlank String email) {
        return userService.existsByEmail(email);
    }

    @GetMapping("/auth/checkUsername")
    public boolean checkUsernameExistence(
            @RequestParam
            @Size(min = 3, max = 10)
            String username
    ) {
        return userService.existsByUsername(username);
    }

    @PostMapping("/auth/refresh")
    public ResponseRefreshDto refreshToken(@Valid @RequestBody RequestRefreshDto dto) {
        return authenticationService.refreshToken(dto);
    }

    @DeleteMapping("/auth/refreshToken")
    public void deleteRefreshToken() {
        authenticationService.deleteRefreshToken();
    }
}
