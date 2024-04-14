package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import com.example.beautybook.dto.servicecard.ServiceCardSearchDto;
import com.example.beautybook.model.Photo;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.model.Subcategory;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        config = MapperConfig.class,
        uses = {
                PhotoMapper.class,
                SubcategoryMapper.class,
                AddressMapper.class
        })
public interface ServiceCardMapper {
    @Mapping(target = "masterCardId", source = "masterCard.id")
    @Mapping(target = "subcategoryId", source = "subcategory.id")
    ServiceCardDto toDto(ServiceCard serviceCard);

    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "photo", ignore = true)
    ServiceCard toModel(ServiceCardCreateDto serviceCardCreateDto);

    @Mapping(target = "subcategory", source = "subcategory.name")
    @Mapping(target = "photo", source = "photo.photoUrl")
    ServiceCardSearchDto toSearchDto(ServiceCard serviceCard);

    void updateServiceCardForDto(@MappingTarget ServiceCard serviceCard, ServiceCardCreateDto dto);

    @AfterMapping
    default void setSubcategoryAndPhoto(
            @MappingTarget ServiceCard serviceCard,
            ServiceCardCreateDto dto) {
        serviceCard.setSubcategory(new Subcategory(dto.getSubcategoryId()));
        if (dto.getPhotoId() != null) {
            serviceCard.setPhoto(new Photo(dto.getPhotoId()));
        }
    }
}
