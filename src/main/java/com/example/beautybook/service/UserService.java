package com.example.beautybook.service;

import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.dto.user.UserUpdateDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto createUser(UserRegistrationDto dto);

    UserDto update(UserUpdateDto userUpdateDto);

    UserDto uploadProfilePhoto(MultipartFile file);

    UserDto addFavoriteMasterCard(Long id);

    UserDto deleteFavoriteMasterCard(Long id);

    void forgotPassword(String email);
}
