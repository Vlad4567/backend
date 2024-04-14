package com.example.beautybook.security;

import com.example.beautybook.dto.TelegramLoginDto;
import com.example.beautybook.dto.user.request.RequestRefreshDto;
import com.example.beautybook.dto.user.request.UserLoginRequestDto;
import com.example.beautybook.dto.user.response.ResponseRefreshDto;
import com.example.beautybook.dto.user.response.UserLoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    UserLoginResponseDto authentication(UserLoginRequestDto loginRequestDto);

    boolean verificationEmail(String token);

    ResponseRefreshDto refreshToken(RequestRefreshDto requestRefreshDto);

    void deleteRefreshToken();

    UserLoginResponseDto loginWithTelegram(TelegramLoginDto dto);

    String getLoginDeviceToken(HttpServletRequest request);
}
