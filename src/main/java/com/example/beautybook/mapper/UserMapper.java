package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.user.request.UserRegistrationDto;
import com.example.beautybook.dto.user.request.UserUpdateDto;
import com.example.beautybook.dto.user.response.UserDto;
import com.example.beautybook.dto.user.response.UserPreviewDto;
import com.example.beautybook.dto.user.response.UserUpdateResponseDto;
import com.example.beautybook.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        config = MapperConfig.class,
        uses = TelegramAccountMapper.class
)
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toModel(UserRegistrationDto userRegistrationDto);

    @Mapping(target = "username", ignore = true)
    UserDto toDto(User user);

    @Mapping(target = "username", ignore = true)
    UserPreviewDto toPreviewDto(User user);

    @Mapping(target = "username", ignore = true)
    UserUpdateResponseDto toUpdateResponseDto(User user);

    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

    @AfterMapping
    default void mapUserName(@MappingTarget UserDto userDto, User user) {
        userDto.setUsername(user.getUserName());
    }

    @AfterMapping
    default void mapUserName(@MappingTarget UserUpdateResponseDto dto, User user) {
        dto.setUsername(user.getUserName());
    }

    @AfterMapping
    default void mapUserName(@MappingTarget UserPreviewDto dto, User user) {
        dto.setUsername(user.getUserName());
    }
}
