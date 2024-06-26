package com.example.beautybook.service;

import com.example.beautybook.dto.photo.PhotoDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    PhotoDto savePhoto(MultipartFile file, Long subcategoryId);

    void deletePhoto(Long id);

    void updateMainPhoto(Long id);

    Page<PhotoDto> getPhotoByMasterCardAndSubcategory(
            Pageable pageable, Long subcategoryId, Long masterCardId);

    List<PhotoDto> getRandomPhotoByMasterCard(Long masterCardId);
}
