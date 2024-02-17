package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.model.Subcategory;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ServiceCardMapper {
    @Mapping(target = "masterCardId", source = "masterCard.id")
    @Mapping(target = "subcategoryId", source = "subcategory.id")
    ServiceCardDto toDto(ServiceCard serviceCard);

    @Mapping(target = "subcategory", ignore = true)
    ServiceCard toModel(ServiceCardCreateDto serviceCardCreateDto);

    @AfterMapping
    default void setSubcategory(
            @MappingTarget ServiceCard serviceCard,
            ServiceCardCreateDto dto) {
        serviceCard.setSubcategory(new Subcategory(dto.getSubcategoryId()));
    }
}
