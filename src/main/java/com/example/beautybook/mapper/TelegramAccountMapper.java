package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.TelegramAccountDto;
import com.example.beautybook.model.TelegramAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface TelegramAccountMapper {
    @Mapping(target = "telegramUsername", source = "username")
    TelegramAccountDto toDto(TelegramAccount telegramAccount);
}
