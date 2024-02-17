package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        config = MapperConfig.class,
        uses = {
                PhotoMapper.class,
                ReviewMapper.class,
                LocationMapper.class,
                SubcategoryMapper.class,
                AddressMapper.class,
                ServiceCardMapper.class
        })
public interface MasterCardMapper {
    MasterCard toModel(MasterCardUpdateDto masterCardUpdateDto);

    @Mapping(target = "userId", source = "user.id")
    MasterCardDto toDto(MasterCard masterCard);

    @Mapping(target = "subcategories", ignore = true)
    MasterCardResponseDto toResponseDto(MasterCard masterCard);

    void updateModelForDto(@MappingTarget MasterCard masterCard, MasterCardUpdateDto dto);

    @AfterMapping
    default User mapUserIdToUser(Long userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }
}
