package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.mastercard.MasterCardCreateDto;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.User;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface MasterCardMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "location", source = "locationDto")
    @Mapping(target = "workPeriod", source = "workPeriodDto")
    MasterCard toNewModel(MasterCardCreateDto masterCardCreateDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "gallery", ignore = true)
    MasterCardDto toDto(MasterCard masterCard);

    @AfterMapping
    default User mapUserIdToUser(Long userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    @AfterMapping
    default void setPhotoDto(
            @MappingTarget MasterCardDto masterCardDto,
            MasterCard masterCard
    ) {
        masterCardDto.setGallery(masterCard.getGallery().stream()
                .map(photo -> new PhotoDto(photo.getPhotoUrl(), photo.getDescription()))
                .collect(Collectors.toSet()));
    }
}
