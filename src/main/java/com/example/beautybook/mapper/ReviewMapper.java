package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.review.ReviewCreateDto;
import com.example.beautybook.dto.review.ReviewDto;
import com.example.beautybook.dto.review.ReviewUpdateDto;
import com.example.beautybook.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = UserMapper.class)
public interface ReviewMapper {
    Review toModel(ReviewCreateDto createDto);

    @Mapping(target = "masterCardId", source = "masterCard.id")
    ReviewDto toDto(Review review);

    void updateReviewFromDto(ReviewUpdateDto dto, @MappingTarget Review review);

}
