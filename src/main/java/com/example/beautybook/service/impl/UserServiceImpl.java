package com.example.beautybook.service.impl;

import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.dto.user.UserUpdateDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.RegistrationException;
import com.example.beautybook.mapper.UserMapper;
import com.example.beautybook.model.Role;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.RoleRepository;
import com.example.beautybook.repository.UserRepository;
import com.example.beautybook.service.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with email already exists");
        }
        User newUser = userMapper.toModel(userRegistrationDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findByName(Role.RoleName.CUSTOMER)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Not found role by name: " + Role.RoleName.CUSTOMER)));
        newUser.setRoles(roles);
        return userMapper.toDto(userRepository.save(newUser));
    }

    @Override
    public UserDto update(UserUpdateDto userUpdateDto) {
        return null;
    }
}
