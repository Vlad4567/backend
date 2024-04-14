package com.example.beautybook.dto.servicecard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ServiceCardCreateDto {
    @NotBlank(message = "Сan not be empty")
    private String name;
    @NotNull(message = "Сan not be empty")
    private BigDecimal price;
    private int duration;
    @NotNull(message = "Сan not be empty")
    private Long subcategoryId;
    private Long photoId;
}
