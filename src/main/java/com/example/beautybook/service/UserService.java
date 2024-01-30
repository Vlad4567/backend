package com.example.beautybook.service;

import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.dto.user.UserUpdateDto;

public interface UserService {
    UserDto createUser(UserRegistrationDto dto);

    UserDto update(UserUpdateDto userUpdateDto);
}
