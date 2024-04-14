package com.example.beautybook.service;

import com.example.beautybook.dto.user.request.ResetPasswordDto;
import com.example.beautybook.dto.user.request.UpdateEmailDto;
import com.example.beautybook.dto.user.request.UserRegistrationDto;
import com.example.beautybook.dto.user.request.UserUpdateDto;
import com.example.beautybook.dto.user.response.UserDto;
import com.example.beautybook.dto.user.response.UserUpdateResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto createUser(UserRegistrationDto dto);

    UserUpdateResponseDto update(UserUpdateDto userUpdateDto);

    String uploadProfilePhoto(MultipartFile file);

    UserDto getAuthenticationUser();

    UserDto resetPassword(ResetPasswordDto resetPasswordDto);

    void forgotPassword(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void updateEmail(String token);

    void verificationNewMail(UpdateEmailDto updateEmailDto);
}
