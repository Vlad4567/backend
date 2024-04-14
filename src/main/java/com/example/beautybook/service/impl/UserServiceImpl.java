package com.example.beautybook.service.impl;

import com.example.beautybook.dto.user.request.ResetPasswordDto;
import com.example.beautybook.dto.user.request.UpdateEmailDto;
import com.example.beautybook.dto.user.request.UserRegistrationDto;
import com.example.beautybook.dto.user.request.UserUpdateDto;
import com.example.beautybook.dto.user.response.UserDto;
import com.example.beautybook.dto.user.response.UserUpdateResponseDto;
import com.example.beautybook.exceptions.AccessDeniedException;
import com.example.beautybook.exceptions.DataConflictException;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.RegistrationException;
import com.example.beautybook.exceptions.VirusDetectionException;
import com.example.beautybook.mapper.UserMapper;
import com.example.beautybook.message.MessageProvider;
import com.example.beautybook.model.Role;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.security.JwtUtil;
import com.example.beautybook.service.UserService;
import com.example.beautybook.util.EmailSenderUtil;
import com.example.beautybook.util.PasswordGeneratorUtil;
import com.example.beautybook.util.UploadFileUtil;
import com.example.beautybook.util.VirusScannerUtil;
import com.example.beautybook.util.impl.ImageUtil;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String API = "/auth/verificationMail/";
    private static final String API_UPDATE_EMAIL = "/user/updateEmail/";
    private static final String IMAGE_FORMAT = ".jpg";
    private static final String FILE_NAME = "userProfilePhoto";
    private static final int WIDTH_PROFILE_PHOTO = 162;
    private static final int HEIGHT_PROFILE_PHOTO = 162;
    private static final int WIDTH_PROFILE_PHOTO_REVIEW = 44;
    private static final int HEIGHT_PROFILE_PHOTO_REVIEW = 44;
    private static final String DELIMITER_EMAIL = ":";
    private static final int INDEX_EMAIL = 0;
    private static final int INDEX_NEW_EMAIL = 1;
    @Value("${path.host}")
    private String host;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final UserRepository userRepository;
    private final MasterCardRepository masterCardRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UploadFileUtil uploadFileUtil;
    private final VirusScannerUtil virusScannerUtil;
    private final PasswordGeneratorUtil passwordGenerator;
    private final EmailSenderUtil emailSenderUtil;
    @Value("${uploud.dir}")
    private String uploadDir;

    @Override
    @Transactional
    public UserDto createUser(UserRegistrationDto userRegistrationDto) {
        User newUser = userMapper.toModel(userRegistrationDto);
        checkExistingCredentials(newUser.getEmail(), newUser.getUserName());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L));
        newUser.setRoles(roles);
        newUser.setUuid(UUID.randomUUID().toString());
        User user = userRepository.save(newUser);
        user.setUuid(user.getUuid() + user.getId());
        userRepository.save(user);

        String link = host + contextPath + API
                + jwtUtil.generateToken(user.getEmail(), JwtUtil.Secret.MAIL);
        String text = MessageProvider.getMessage(
                "verification.email", user.getUserName(), link);
        emailSenderUtil.sendEmail(user.getEmail(), "Verification email", text);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserUpdateResponseDto update(UserUpdateDto userUpdateDto) {
        User user = getAuthenticatedUser();
        updateUsername(user, userUpdateDto.getUsername());

        UserUpdateResponseDto responseDto =
                userMapper.toUpdateResponseDto(userRepository.save(user));
        responseDto.setMaster(masterCardRepository.existsByUserEmail(responseDto.getEmail()));
        checkNewEmail(responseDto, userUpdateDto.getEmail());
        return responseDto;
    }

    @Override
    @Transactional
    public String uploadProfilePhoto(MultipartFile file) {
        User user = getAuthenticatedUser();
        String fileName = FILE_NAME + user.getId() + IMAGE_FORMAT;
        String path = uploadDir + fileName;
        uploadFileUtil.uploadFile(file, path);
        if (virusScannerUtil.scanFile(path) instanceof ScanResult.VirusFound) {
            throw new VirusDetectionException("Virus detected in the uploaded photo.");
        }

        savePhotosWithResizedDimensions(path, user.getId());
        user.setProfilePhoto(fileName);
        userRepository.save(user);
        return fileName;
    }

    @Override
    @Transactional
    public UserDto getAuthenticationUser() {
        UserDto dto = userMapper.toDto(getAuthenticatedUser());
        dto.setMaster(masterCardRepository.existsByUserEmail(dto.getEmail()));
        return dto;
    }

    @Override
    @Transactional
    public UserDto resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = getAuthenticatedUser();
        if (!passwordEncoder.matches(resetPasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new AccessDeniedException("The current password is not valid");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by email: " + email)
                );
        String password = passwordGenerator.generateRandomPassword();
        String text =
                MessageProvider.getMessage("password.reset", user.getUserName(), password);
        emailSenderUtil.sendEmail(user.getEmail(), "Password Reset", text);
        user.setPassword(passwordEncoder.encode(password));
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
    @Transactional
    public void updateEmail(String token) {
        if (jwtUtil.isValidToken(token, JwtUtil.Secret.MAIL)) {
            String emails = jwtUtil.getUsername(token, JwtUtil.Secret.MAIL);
            String email = emails.split(DELIMITER_EMAIL)[INDEX_EMAIL];
            String newEmail = emails.split(DELIMITER_EMAIL)[INDEX_NEW_EMAIL];
            User user = userRepository.findByUuid(email).orElseThrow(
                    () -> new EntityNotFoundException("Not found user by email " + email));
            user.setEmail(newEmail);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void verificationNewMail(UpdateEmailDto updateEmailDto) {
        User user = getAuthenticatedUser();
        if (!passwordEncoder.matches(updateEmailDto.getPassword(), user.getPassword())) {
            throw new AccessDeniedException("Incorrect password");
        }
        String newEmail = updateEmailDto.getNewEmail();
        String emails = user.getEmail() + DELIMITER_EMAIL + newEmail;

        String link = host + contextPath + API_UPDATE_EMAIL
                + jwtUtil.generateToken(emails, JwtUtil.Secret.MAIL);
        String text = MessageProvider.getMessage(
                "verification.email", user.getUserName(), link);
        emailSenderUtil.sendEmail(newEmail, "Verification email", text);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUuid(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Not fount user by email: "
                        + authentication.getName()));
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

    private void updateUsername(User user, String username) {
        if (!username.equals(user.getUserName())) {
            if (existsByUsername(username)) {
                throw new DataConflictException(
                        "Username '" + username + "' already exists.");
            }
            user.setUsername(username);
        }
    }

    private void checkNewEmail(UserUpdateResponseDto dto, String newEmail) {
        if (!newEmail.equals(dto.getEmail())) {
            if (existsByEmail(newEmail)) {
                throw new DataConflictException(
                        "Email '" + newEmail + "' already exists.");
            }
            dto.setNewEmail(newEmail);
        }
    }

    private void savePhotosWithResizedDimensions(String path, Long userId) {
        BufferedImage image = ImageUtil.readImage(path);
        String fileNamePhotoReview = FILE_NAME + userId + "R" + IMAGE_FORMAT;
        String pathPhotoReview = uploadDir + fileNamePhotoReview;
        ImageUtil.resizeImage(image, path, WIDTH_PROFILE_PHOTO, HEIGHT_PROFILE_PHOTO);
        ImageUtil.resizeImage(
                image,
                pathPhotoReview,
                WIDTH_PROFILE_PHOTO_REVIEW,
                HEIGHT_PROFILE_PHOTO_REVIEW
        );
    }
}
