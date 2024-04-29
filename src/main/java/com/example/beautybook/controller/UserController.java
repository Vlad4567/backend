package com.example.beautybook.controller;

import com.example.beautybook.dto.user.request.ResetPasswordDto;
import com.example.beautybook.dto.user.request.UpdateEmailDto;
import com.example.beautybook.dto.user.request.UserUpdateDto;
import com.example.beautybook.dto.user.response.UserDto;
import com.example.beautybook.dto.user.response.UserUpdateResponseDto;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.service.UserService;
import com.example.beautybook.validation.ImageFile;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Content-Type", "Authorization"},
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS}
)
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/profilePhoto")
    public String uploadProfilePhoto(
            @RequestParam("file")
            @Valid
            @ImageFile()
            MultipartFile file
    ) {
        return userService.uploadProfilePhoto(file);
    }

    @PutMapping("/user")
    public UserUpdateResponseDto update(@RequestBody @Valid UserUpdateDto userUpdateDto) {
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

    @GetMapping("/user/updateEmail/{token}")
    public ResponseEntity<Resource> updateEmail(@PathVariable String token) {
        userService.updateEmail(token);
        Resource resource = new ClassPathResource("templates/verEm.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }

    @PutMapping("/user/verificationNewMail")
    public String verificationNewMail(@RequestBody UpdateEmailDto updateEmailDto) {
        userService.verificationNewMail(updateEmailDto);
        return "ok";
    }
}
