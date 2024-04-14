package com.example.beautybook.service.impl;

import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.dto.review.ReviewCreateDto;
import com.example.beautybook.dto.review.ReviewDto;
import com.example.beautybook.dto.review.ReviewUpdateDto;
import com.example.beautybook.exceptions.AccessDeniedException;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.mapper.ReviewMapper;
import com.example.beautybook.message.MessageProvider;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final MasterCardRepository masterCardRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;
    private final TelegramBot telegramBot;

    @Override
    @Transactional
    public ReviewDto createReview(Long id, ReviewCreateDto createDto) {
        User user = getAuthenticatedUser();
        System.out.println(user.toString());
        MasterCard masterCard = masterCardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found master card by id: " + id));

        if (user.getId().equals(masterCard.getUser().getId())) {
            throw new AccessDeniedException("Cannot leave a review for yourself");
        }
        Review newReview = reviewMapper.toModel(createDto);
        newReview.setMasterCard(masterCard);
        newReview.setUser(user);
        Review review = reviewRepository.save(newReview);
        updateMasterCardRating(id);
        if (masterCard.getUser().getTelegramAccount() != null) {
            sendReviewToTelegram(review);
        }
        return reviewMapper.toDto(review);
    }

    @Override
    @Transactional
    public ReviewDto update(Long id, ReviewUpdateDto reviewUpdateDto) {
        User user = getAuthenticatedUser();
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found review by id: " + id));
        if (!user.getId().equals(review.getUser().getId())) {
            throw new AccessDeniedException("You don't have permission to update this review.");
        }
        reviewMapper.updateReviewFromDto(reviewUpdateDto, review);
        review = reviewRepository.save(review);
        updateMasterCardRating(review.getMasterCard().getId());
        return reviewMapper.toDto(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto> getAllByMasterCardId(Long id, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByMasterCardId(id, pageable);
        List<ReviewDto> list = reviews.getContent().stream().map(reviewMapper::toDto).toList();
        return new PageImpl<>(list, reviews.getPageable(), reviews.getTotalElements());
    }

    private void updateMasterCardRating(Long masterCardId) {
        BigDecimal averageRating =
                BigDecimal.valueOf(reviewRepository.getAverageRatingByMasterId(masterCardId))
                        .setScale(1, RoundingMode.HALF_UP);
        masterCardRepository.updateRating(masterCardId, averageRating);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUuid(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Not fount user by email: "
                        + authentication.getName()));
    }

    private void sendReviewToTelegram(Review review) {
        Long chatId = review.getMasterCard().getUser().getTelegramAccount().getChatId();
        String text = MessageProvider.getMessage(
                "new.review",
                review.getId(),
                review.getUser().getUserName(),
                "⭐️".repeat(review.getGrade()),
                review.getComment()
        );
        telegramBot.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .parseMode(ParseMode.MARKDOWN)
                        .replyMarkup(createKeyboard())
                        .build()
        );
    }

    private InlineKeyboardMarkup createKeyboard() {
        return InlineKeyboardMarkup.builder().keyboardRow(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("View")
                                .url("https://www.google.com/")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Reply")
                                .callbackData("#reply:review")
                                .build()
                )
        ).build();
    }
}
