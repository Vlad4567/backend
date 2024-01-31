package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.location.LocationDto;
import com.example.beautybook.dto.workperiod.WorkPeriodDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MasterCardCreateDto {
    @NotNull
    private Long userId;
    @NotBlank
    private String address;
    @NotNull
    private LocationDto locationDto;
    private WorkPeriodDto workPeriodDto;
    private String description;
}
