package com.example.beautybook.security;

import com.example.beautybook.dto.user.UserLoginRequestDto;
import com.example.beautybook.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authentication(UserLoginRequestDto loginRequestDto);
}
