package com.example.beautybook.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<ImageFile, MultipartFile> {
    @Override
    public void initialize(ImageFile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            return isImage(value.getInputStream());
        } catch (ImageReadException | IOException e) {
            throw new RuntimeException("Failed to determine image type: " + e.getMessage(), e);
        }
    }

    private boolean isImage(InputStream inputStream) throws IOException, ImageReadException {
        ImageInfo imageInfo = Imaging.getImageInfo(inputStream, null);
        ImageInfo.CompressionAlgorithm imageType = imageInfo.getCompressionAlgorithm();
        return imageType != ImageInfo.CompressionAlgorithm.UNKNOWN;

    }
}
