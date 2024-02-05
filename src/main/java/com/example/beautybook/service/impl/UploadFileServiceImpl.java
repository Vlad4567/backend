package com.example.beautybook.service.impl;

import com.example.beautybook.exceptions.FileUploadException;
import com.example.beautybook.exceptions.photo.EmptyPhotoException;
import com.example.beautybook.service.UploadFileService;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Override
    public String uploadFile(MultipartFile file, String path) {
        if (file.isEmpty()) {
            throw new EmptyPhotoException("File is Empty");
        }
        try {
            File destFile = new File(path);
            file.transferTo(destFile);
            return path;
        } catch (IOException e) {
            throw new FileUploadException("Error uploading the file.", e);
        }
    }
}
