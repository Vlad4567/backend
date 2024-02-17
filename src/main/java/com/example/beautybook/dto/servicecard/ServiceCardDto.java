package com.example.beautybook.dto.servicecard;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ServiceCardDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private int duration;
    private Long subcategoryId;
    private Long masterCardId;
}
