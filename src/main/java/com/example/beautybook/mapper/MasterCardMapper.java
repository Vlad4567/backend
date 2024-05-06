package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.mastercard.MasterCardCreateDto;
import com.example.beautybook.dto.mastercard.MasterCardCreateResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardPreviewDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateResponseDto;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.Subcategory;
import com.example.beautybook.model.User;
import java.util.Set;
import java.util.stream.Collectors;
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
                ServiceCardMapper.class,
                ContactsMapper.class
        })
public interface MasterCardMapper {
    @Mapping(target = "subcategories", ignore = true)
    MasterCard toModel(MasterCardCreateDto masterCardCreateDto);

    @Mapping(target = "mainPhoto", source = "mainPhoto.photoUrl")
    MasterCardDto toDto(MasterCard masterCard);

    MasterCardUpdateResponseDto toUpdateResponseDto(MasterCard masterCard);

    MasterCardCreateResponseDto toCreateResponseDto(MasterCard masterCard);

    MasterCardPreviewDto toPreviewDto(MasterCard masterCard);

    @Mapping(target = "mainPhoto", source = "mainPhoto.photoUrl")
    MasterCardResponseDto toResponseDto(MasterCard masterCard);

    void updateModelForDto(@MappingTarget MasterCard masterCard, MasterCardUpdateDto dto);

    @AfterMapping
    default User mapUserIdToUser(Long userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    @AfterMapping
    default void setSubcategoriesToModel(
            @MappingTarget
            MasterCard masterCard,
            MasterCardCreateDto dto
    ) {
        if (dto.getSubcategories() != null) {
            masterCard.setSubcategories(
                    dto.getSubcategories().stream()
                            .map(Subcategory::new)
                            .collect(Collectors.toSet())
            );
        }
    }
}
