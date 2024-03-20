package com.example.beautybook.service;

import com.example.beautybook.dto.photo.PhotoDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    PhotoDto savePhoto(MultipartFile file, Long subcategoryId);

    void deletePhoto(Long id);

    void updateMainPhoto(Long id);

    List<PhotoDto> getPhotoByMasterCardAndSubcategory(Long subcategoryId, Long masterCardId);
}
