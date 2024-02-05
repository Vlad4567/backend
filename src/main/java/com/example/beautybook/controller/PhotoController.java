package com.example.beautybook.controller;

import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.service.PhotoService;
import com.example.beautybook.validation.ImageFile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping("/upload")
    public PhotoDto handleFileUpload(
            @RequestParam("file")
            @Valid
            @ImageFile
            MultipartFile file
    ) {
        return photoService.savePhoto(file);
    }
}