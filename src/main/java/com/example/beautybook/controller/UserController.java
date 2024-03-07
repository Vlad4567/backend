package com.example.beautybook.controller;

import com.example.beautybook.dto.user.ResetPasswordDto;
import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserUpdateDto;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.service.UserService;
import com.example.beautybook.validation.ImageFile;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Content-Type", "Authorization"})
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/profilePhoto")
    public UserDto uploadProfilePhoto(
            @RequestParam("file")
            @Valid
            @ImageFile
            MultipartFile file
    ) {
        return userService.uploadProfilePhoto(file);
    }

    @PostMapping("/favorite/{id}")
    public UserDto addFavoriteMasterCard(@PathVariable Long id) {
        return userService.addFavoriteMasterCard(id);
    }

    @PutMapping("/favorite/{id}")
    public UserDto deleteFavoriteMasterCard(@PathVariable Long id) {
        return userService.deleteFavoriteMasterCard(id);
    }

    @PutMapping("/updateUser")
    public UserDto update(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        return userService.update(userUpdateDto);
    }

    @GetMapping("/getEmail")
    public List<String> getEmail() {
        return userRepository.findAllEmail();
    }

    @GetMapping("/user")
    public UserDto getAuthenticationUser() {
        return userService.getAuthenticationUser();
    }

    @PutMapping("/reset-password")
    public UserDto resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto) {
        return userService.resetPassword(resetPasswordDto);
    }
}
