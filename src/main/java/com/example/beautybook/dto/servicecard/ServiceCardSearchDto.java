package com.example.beautybook.dto.servicecard;

import com.example.beautybook.dto.mastercard.MasterCardPreviewDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ServiceCardSearchDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String subcategory;
    private String photo;
    private MasterCardPreviewDto masterCard;
}
