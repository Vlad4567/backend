package com.example.beautybook.service;

import com.example.beautybook.dto.review.ReviewCreateDto;
import com.example.beautybook.dto.review.ReviewDto;
import com.example.beautybook.dto.review.ReviewUpdateDto;
import java.util.List;

public interface ReviewService {
    ReviewDto createReview(Long id, ReviewCreateDto createDto);

    ReviewDto update(Long id, ReviewUpdateDto reviewUpdateDto);

    List<ReviewDto> getAllByMasterCardId(Long id);
}
