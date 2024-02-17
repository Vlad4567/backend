package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.address.AddressDto;
import com.example.beautybook.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    Address toModel(AddressDto dto);

    AddressDto toDto(Address address);

    void update(@MappingTarget Address address, AddressDto dto);
}
