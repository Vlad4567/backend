package com.example.beautybook.util;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileUtil {
    String uploadFile(MultipartFile file, String fileName);
}
