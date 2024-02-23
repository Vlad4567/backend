package com.example.beautybook.service.impl;

import com.example.beautybook.dto.review.ReviewCreateDto;
import com.example.beautybook.dto.review.ReviewDto;
import com.example.beautybook.dto.review.ReviewUpdateDto;
import com.example.beautybook.exceptions.AccessDeniedException;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.mapper.ReviewMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.Review;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.mastercard.ReviewRepository;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.service.ReviewService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import javax.security.auth.login.AccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final MasterCardRepository masterCardRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDto createReview(Long id, ReviewCreateDto createDto) {
        User user = getAuthenticatedUser();
        Review newReview = reviewMapper.toModel(createDto);
        newReview.setMasterCard(new MasterCard(id));
        newReview.setUser(user);
        Review review = reviewRepository.save(newReview);
        updateMasterCardRating(review.getMasterCard());
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDto update(Long id, ReviewUpdateDto reviewUpdateDto) {
        User user = getAuthenticatedUser();
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found review by id: " + id));
        if (!user.getId().equals(review.getUser().getId())) {
            throw new AccessDeniedException("You don't have permission to update this review.");
        }
        reviewMapper.updateReviewFromDto(reviewUpdateDto, review);
        review = reviewRepository.save(review);
        updateMasterCardRating(review.getMasterCard());
        return reviewMapper.toDto(review);
    }

    @Override
    public List<ReviewDto> getAllByMasterCardId(Long id) {
        return reviewRepository.findAllByMasterCardId(id).stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    private void updateMasterCardRating(MasterCard masterCard) {
        BigDecimal averageRating =
                BigDecimal.valueOf(reviewRepository.getAverageRatingByMasterId(masterCard.getId()))
                        .setScale(1, RoundingMode.HALF_UP);
        masterCard.setRating(averageRating);
        masterCardRepository.save(masterCard);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Not fount user by email: "
                        + authentication.getName()));
    }
}
