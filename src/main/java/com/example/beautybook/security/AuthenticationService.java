package com.example.beautybook.security;

import com.example.beautybook.dto.user.RequestRefreshDto;
import com.example.beautybook.dto.user.ResponseRefreshDto;
import com.example.beautybook.dto.user.UserLoginRequestDto;
import com.example.beautybook.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authentication(UserLoginRequestDto loginRequestDto);

    boolean verificationEmail(String token);

    ResponseRefreshDto refreshToken(RequestRefreshDto requestRefreshDto);

    void deleteRefreshToken();
}
