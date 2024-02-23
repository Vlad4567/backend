package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.user.UserDto;
import com.example.beautybook.dto.user.UserRegistrationDto;
import com.example.beautybook.dto.user.UserUpdateDto;
import com.example.beautybook.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toModel(UserRegistrationDto userRegistrationDto);

    @Mapping(target = "username", ignore = true)
    UserDto toDto(User user);

    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

    @AfterMapping
    default void mapUserName(@MappingTarget UserDto userDto, User user) {
        userDto.setUsername(user.getUserName());
    }
}
