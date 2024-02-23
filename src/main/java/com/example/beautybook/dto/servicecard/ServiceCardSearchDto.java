package com.example.beautybook.dto.servicecard;

import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ServiceCardSearchDto {
    private String name;
    private BigDecimal price;
    private String subcategory;
    private MasterCardResponseDto masterCard;
}
