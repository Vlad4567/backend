package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.model.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PhotoMapper {
    @Mapping(target = "isMain", source = "main")
    PhotoDto toDto(Photo photo);
}
