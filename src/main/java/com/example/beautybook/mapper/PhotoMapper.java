package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.model.Photo;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PhotoMapper {
    PhotoDto toDto(Photo photo);
}
