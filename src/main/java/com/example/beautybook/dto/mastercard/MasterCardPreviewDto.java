package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.address.AddressResponseDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class MasterCardPreviewDto {
    private Long id;
    private String firstName;
    private String lastName;
    private AddressResponseDto address;
    private BigDecimal rating;
}
