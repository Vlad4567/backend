package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.location.LocationDto;
import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.dto.workperiod.WorkPeriodDto;
import java.util.Set;
import lombok.Data;

@Data
public class MasterCardDto {
    private Long userId;
    private String address;
    private LocationDto location;
    private WorkPeriodDto workPeriod;
    private String profilePhoto;
    private Set<PhotoDto> gallery;
    private String description;
}
