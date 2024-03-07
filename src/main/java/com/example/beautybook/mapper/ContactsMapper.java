package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.contact.ContactDto;
import com.example.beautybook.model.Contacts;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ContactsMapper {
    Contacts toModel(ContactDto contactDto);
}
