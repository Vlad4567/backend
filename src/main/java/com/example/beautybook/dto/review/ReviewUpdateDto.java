package com.example.beautybook.dto.review;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewUpdateDto {
    @NotNull(message = "Сan not be empty")
    private Integer grade;
    private String comment;
}
