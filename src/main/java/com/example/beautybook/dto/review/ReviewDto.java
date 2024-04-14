package com.example.beautybook.dto.review;

import com.example.beautybook.dto.user.response.UserPreviewDto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private UserPreviewDto user;
    private int grade;
    private String comment;
    private Long masterCardId;
    private LocalDateTime dateTime;
}
