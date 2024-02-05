package com.example.beautybook.exceptions.photo;

public class GalleryLimitExceededException extends RuntimeException {
    public GalleryLimitExceededException(String message) {
        super(message);
    }
}
