package com.example.beautybook.service.impl;

import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.dto.user.UserUpdateDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.RegistrationException;
import com.example.beautybook.exceptions.VirusDetectionException;
import com.example.beautybook.exceptions.photo.InvalidOriginFileNameException;
import com.example.beautybook.mapper.UserMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.Role;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.user.RoleRepository;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.security.JwtUtil;
import com.example.beautybook.service.EmailService;
import com.example.beautybook.service.UserService;
import com.example.beautybook.util.PasswordGeneratorUtil;
import com.example.beautybook.util.UploadFileUtil;
import com.example.beautybook.util.VirusScannerUtil;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MasterCardRepository masterCardRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UploadFileUtil uploadFileUtil;
    private final VirusScannerUtil virusScannerUtil;
    private final EmailService emailService;
    private final PasswordGeneratorUtil passwordGenerator;
    @Value("${uploud.dir}")
    private String uploadDir;

    @Override
    public UserDto createUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with email already exists");
        }
        User newUser = userMapper.toModel(userRegistrationDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findByName(Role.RoleName.UNVERIFIED)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Not found role by name: " + Role.RoleName.UNVERIFIED)));
        newUser.setRoles(roles);
        emailService.sendMail(newUser,"verification");
        return userMapper.toDto(userRepository.save(newUser));
    }

    @Override
    public UserDto update(UserUpdateDto userUpdateDto) {
        User user = getAuthenticatedUser();
        if (userUpdateDto.getPassword() != null) {
            userUpdateDto.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }
        userMapper.updateUserFromDto(userUpdateDto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto uploadProfilePhoto(MultipartFile file) {
        User user = getAuthenticatedUser();
        String path = uploadDir + user.getUsername() + getFileExtension(file);
        uploadFileUtil.uploadFile(file, path);
        if (virusScannerUtil.scanFile(path) instanceof ScanResult.VirusFound) {
            throw new VirusDetectionException("Virus detected in the uploaded photo.");
        }
        user.setProfilePhoto(path);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto addFavoriteMasterCard(Long id) {
        User user = getAuthenticatedUser();
        MasterCard masterCard = masterCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found master card by id: " + id)
                );
        user.getFavorite().add(masterCard);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto deleteFavoriteMasterCard(Long id) {
        User user = getAuthenticatedUser();
        MasterCard masterCard = masterCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found master card by id: " + id)
                );
        user.getFavorite().remove(masterCard);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by email: " + email)
                );
        user.setPassword(passwordGenerator.generateRandomPassword());
        emailService.sendMail(user, "passwordReset");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Not fount user by email: "
                        + authentication.getName()));
    }

    private String getFileExtension(MultipartFile file) {
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new InvalidOriginFileNameException("Origin file name is blank.");
        }
        String[] fileNameParts = file.getOriginalFilename().split("\\.");
        if (fileNameParts.length < 2) {
            throw new InvalidOriginFileNameException("Invalid origin file name format.");
        }
        return fileNameParts[fileNameParts.length - 1];
    }
}
