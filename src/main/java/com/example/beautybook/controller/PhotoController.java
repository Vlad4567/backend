package com.example.beautybook.controller;

import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.service.PhotoService;
import com.example.beautybook.validation.ImageFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
public class PhotoController {
    private final PhotoService photoService;
    @Value("${uploud.dir}")
    private String path;

    @PostMapping("/mastercard/upload")
    public PhotoDto handleFileUpload(
            @RequestParam("file")
            @Valid
            @ImageFile
            MultipartFile file,
            @Valid
            @NotNull(message = "Сan not be empty")
            Long subcategoryId
    ) {
        return photoService.savePhoto(file, subcategoryId);
    }

    @GetMapping("/photo/download")
    byte[] downloadPhoto(
            @Valid @NotBlank(message = "Сan not be empty")
            String file
    ) {
        if (file.contains(":")) {
            file = file.replace(":", "\\");
        }
        Path filePath = Paths.get(path + file);
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/photo/delete")
    void deletePhoto(
            @Valid
            @NotNull(message = "Сan not be empty")
            Long id
    ) {
        photoService.deletePhoto(id);
    }

    @PutMapping("/photo/update/main")
    void updateMainPhoto(
            @Valid
            @NotNull(message = "Сan not be empty")
            Long id
    ) {
        photoService.updateMainPhoto(id);
    }

    @GetMapping("/photo/subcategory/{subcategoryId}")
    Page<PhotoDto> getPhotoByMasterCardAndSubcategory(
            @NotNull(message = "Сan not be empty")
            @PathVariable
            Long subcategoryId,
            Long masterCardId,
            Pageable pageable
    ) {
        return photoService.getPhotoByMasterCardAndSubcategory(
                pageable,
                subcategoryId,
                masterCardId
        );
    }

    @GetMapping("/photo")
    List<PhotoDto> getRandomPhotoByMaster(Long masterCardId) {
        return photoService.getRandomPhotoByMasterCard(masterCardId);
    }
}
