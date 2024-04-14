package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.address.AddressDto;
import com.example.beautybook.dto.address.AddressResponseDto;
import com.example.beautybook.dto.address.AddressWithCityDto;
import com.example.beautybook.model.Address;
import com.example.beautybook.model.City;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    @Mapping(target = "city", ignore = true)
    Address toModel(AddressDto dto);

    @Mapping(target = "city", source = "city.name")
    AddressResponseDto toDto(Address address);

    AddressWithCityDto toWithCityDto(Address address);

    void update(@MappingTarget Address address, AddressDto dto);

    @AfterMapping
    default void setCityById(@MappingTarget Address address, AddressDto dto) {
        address.setCity(new City(dto.getCityId()));
    }
}
