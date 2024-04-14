package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.contact.ContactDto;
import com.example.beautybook.model.Contacts;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ContactsMapper {
    Contacts toModel(ContactDto contactDto);

    void updateModelForDto(@MappingTarget Contacts contacts, ContactDto dto);
}
