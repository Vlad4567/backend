package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.location.LocationDto;
import com.example.beautybook.model.Location;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface LocationMapper {
    Location toModel(LocationDto locationDto);

    LocationDto toDto(Location location);

}
