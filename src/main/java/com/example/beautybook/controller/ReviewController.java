package com.example.beautybook.controller;

import com.example.beautybook.dto.review.ReviewCreateDto;
import com.example.beautybook.dto.review.ReviewDto;
import com.example.beautybook.dto.review.ReviewUpdateDto;
import com.example.beautybook.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("mastercart/{id}/review")
    public ReviewDto createReview(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            ReviewCreateDto reviewCreateDto
    ) {
        return reviewService.createReview(id, reviewCreateDto);
    }

    @PutMapping("/review/{id}")
    public ReviewDto updateReview(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            ReviewUpdateDto reviewUpdateDto
    ) {
        return reviewService.update(id, reviewUpdateDto);
    }

    @GetMapping("/mastercart/{id}/review")
    List<ReviewDto> getAllByMasterCardId(@PathVariable Long id) {
        return reviewService.getAllByMasterCardId(id);
    }
    ///delete
}
