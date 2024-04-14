package com.example.beautybook.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewCreateDto {
    @Min(value = 1, message = "Can not be less than 1.")
    @Max(value = 5, message = "Can not be greater than 5.")
    private Integer grade;
    @Size(max = 500, message = "Comment must be less than or equal to 500 characters")
    private String comment;
}
