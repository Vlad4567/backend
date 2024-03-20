package com.example.beautybook.service;

import com.example.beautybook.dto.user.ResetPasswordDto;
import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.dto.user.UserUpdateDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto createUser(UserRegistrationDto dto);

    UserDto update(UserUpdateDto userUpdateDto);

    String uploadProfilePhoto(MultipartFile file);

    UserDto addFavoriteMasterCard(Long id);

    UserDto deleteFavoriteMasterCard(Long id);

    UserDto getAuthenticationUser();

    UserDto resetPassword(ResetPasswordDto resetPasswordDto);

    void forgotPassword(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void updateEmail(String token);

    void verificationNewMail(String token);
}
