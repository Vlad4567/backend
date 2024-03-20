package com.example.beautybook.service.impl;

import com.example.beautybook.dto.DataForMailDto;
import com.example.beautybook.dto.user.ResetPasswordDto;
import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.dto.user.UserUpdateDto;
import com.example.beautybook.exceptions.AccessDeniedException;
import com.example.beautybook.exceptions.DataConflictException;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.RegistrationException;
import com.example.beautybook.exceptions.VirusDetectionException;
import com.example.beautybook.exceptions.photo.InvalidOriginFileNameException;
import com.example.beautybook.mapper.UserMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.Role;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.security.JwtUtil;
import com.example.beautybook.service.EmailService;
import com.example.beautybook.service.UserService;
import com.example.beautybook.util.PasswordGeneratorUtil;
import com.example.beautybook.util.UploadFileUtil;
import com.example.beautybook.util.VirusScannerUtil;
import com.example.beautybook.util.impl.ImageUtil;
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
    private static final String IMAGE_FORMAT = ".jpg";
    private static final String FILE_NAME = "userProfilePhoto\\userProfilePhoto";
    private static final int WIDTH_PROFILE_PHOTO = 162;
    private static final int HEIGHT_PROFILE_PHOTO = 162;
    private final UserRepository userRepository;
    private final MasterCardRepository masterCardRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UploadFileUtil uploadFileUtil;
    private final VirusScannerUtil virusScannerUtil;
    private final EmailService emailService;
    private final PasswordGeneratorUtil passwordGenerator;
    @Value("${uploud.dir}")
    private String uploadDir;

    @Override
    public UserDto createUser(UserRegistrationDto userRegistrationDto) {
        User newUser = userMapper.toModel(userRegistrationDto);
        checkExistingCredentials(newUser.getEmail(), newUser.getUserName());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L));
        newUser.setRoles(roles);
        User user = userRepository.save(newUser);
        emailService.sendMail(new DataForMailDto(
                user.getUserName(),
                user.getEmail(),
                "verification"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto update(UserUpdateDto userUpdateDto) {
        User user = getAuthenticatedUser();
        if (!userUpdateDto.getUsername().equals(user.getUserName())) {
            if (existsByUsername(userUpdateDto.getUsername())) {
                throw new DataConflictException(
                        "Username '" + userUpdateDto.getUsername() + "' already exists.");
            }
            user.setUsername(userUpdateDto.getUsername());
        }
        if (!userUpdateDto.getEmail().equals(user.getEmail())) {
            if (existsByEmail(userUpdateDto.getEmail())) {
                throw new DataConflictException(
                        "Email '" + userUpdateDto.getEmail() + "' already exists.");
            }
            emailService.sendMail(new DataForMailDto(
                    user.getUserName(),
                    user.getEmail(),
                    userUpdateDto.getEmail(),
                    "updateEmail")
            );
        }
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public String uploadProfilePhoto(MultipartFile file) {
        User user = getAuthenticatedUser();
        String fileName = FILE_NAME + user.getId() + IMAGE_FORMAT;
        String path = uploadDir + fileName;
        uploadFileUtil.uploadFile(file, path);
        if (virusScannerUtil.scanFile(path) instanceof ScanResult.VirusFound) {
            throw new VirusDetectionException("Virus detected in the uploaded photo.");
        }
        ImageUtil.resizeImage(path, path, WIDTH_PROFILE_PHOTO, HEIGHT_PROFILE_PHOTO);
        fileName = fileName.replace("\\", ":");
        user.setProfilePhoto(fileName);
        userRepository.save(user);
        return fileName;
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
    public UserDto getAuthenticationUser() {
        UserDto dto = userMapper.toDto(getAuthenticatedUser());
        dto.setMaster(masterCardRepository.existsByUserEmail(dto.getEmail()));
        return dto;
    }

    @Override
    public UserDto resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = getAuthenticatedUser();
        if (!passwordEncoder.matches(resetPasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new AccessDeniedException("The current password is not valid");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by email: " + email)
                );
        user.setPassword(passwordGenerator.generateRandomPassword());

        emailService.sendMail(new DataForMailDto(
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                "passwordReset")
        );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void updateEmail(String token) {
        if (jwtUtil.isValidToken(token, JwtUtil.Secret.MAIL)) {
            String emails = jwtUtil.getUsername(token, JwtUtil.Secret.MAIL);
            String newEmail = emails.split(":")[1];
            emailService.sendMail(new DataForMailDto(
                    null,
                    newEmail,
                    emails,
                    "verificationNewEmail")
            );
        }
    }

    @Override
    public void verificationNewMail(String token) {
        if (jwtUtil.isValidToken(token, JwtUtil.Secret.MAIL)) {
            String emails = jwtUtil.getUsername(token, JwtUtil.Secret.MAIL);
            String email = emails.split(":")[0];
            String newEmail = emails.split(":")[1];
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new EntityNotFoundException("Not found user by email " + email));
            user.setEmail(newEmail);
            userRepository.save(user);
        }
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

    private void checkExistingCredentials(String email, String username) {
        boolean emailExists = existsByEmail(email);
        boolean usernameExists = existsByUsername(username);
        if (emailExists && usernameExists) {
            throw new RegistrationException("email:Email already exists " + email
                    + System.lineSeparator()
                    + "username:Username already exists " + username);
        } else if (emailExists) {
            throw new RegistrationException("email:Email already exists " + email);
        } else if (usernameExists) {
            throw new RegistrationException(
                    "username:Username already exists " + username);
        }
    }
}
