package com.example.beautybook.service;

import com.example.beautybook.dto.review.ReviewCreateDto;
import com.example.beautybook.dto.review.ReviewDto;
import com.example.beautybook.dto.review.ReviewUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewDto createReview(Long id, ReviewCreateDto createDto);

    ReviewDto update(Long id, ReviewUpdateDto reviewUpdateDto);

    Page<ReviewDto> getAllByMasterCardId(Long id, Pageable pageable);
}
