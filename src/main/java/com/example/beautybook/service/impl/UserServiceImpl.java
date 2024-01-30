package com.example.beautybook.service.impl;

import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.dto.user.UserUpdateDto;
import com.example.beautybook.exceptions.RegistrationException;
import com.example.beautybook.mapper.UserMapper;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.UserRepository;
import com.example.beautybook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with email already exists");
        }
        User user = userMapper.toModel(userRegistrationDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto update(UserUpdateDto userUpdateDto) {
        return null;
    }
}
