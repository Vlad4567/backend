package com.example.beautybook.dto.servicecard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ServiceCardCreateDto {
    @NotBlank
    private String name;
    @NotNull
    private BigDecimal price;
    private int duration;
    @NotNull
    private Long subcategoryId;
}
