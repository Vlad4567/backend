package com.example.beautybook.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ReviewCreateDto {
    @Min(0)
    @Max(5)
    private Integer grade;
    private String comment;
}
