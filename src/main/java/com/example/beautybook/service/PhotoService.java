package com.example.beautybook.service;

import com.example.beautybook.dto.photo.PhotoDto;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    PhotoDto savePhoto(MultipartFile file, Long subcategoryId);
}
