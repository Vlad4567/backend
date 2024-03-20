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
            @NotNull
            Long subcategoryId
    ) {
        return photoService.savePhoto(file, subcategoryId);
    }

    @GetMapping("/photo/download")
    byte[] downloadPhoto(@Valid @NotBlank String file) {
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
    void deletePhoto(@Valid @NotNull Long id) {
        photoService.deletePhoto(id);
    }

    @PutMapping("/photo/update/main")
    void updateMainPhoto(@Valid @NotNull Long id) {
        photoService.updateMainPhoto(id);
    }

    @GetMapping("/photo/subcategory/{subcategoryId}")
    List<PhotoDto> getPhotoByMasterCardAndSubcategory(
            @NotBlank
            @PathVariable
            Long subcategoryId,
            Long masterCardId
    ) {
        return photoService.getPhotoByMasterCardAndSubcategory(subcategoryId, masterCardId);
    }
}
