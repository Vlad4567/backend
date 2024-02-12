package com.example.beautybook.dto.review;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewUpdateDto {
    @NotNull
    private Integer grade;
    private String comment;
}
