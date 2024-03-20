package com.example.beautybook.util.impl;

import com.example.beautybook.dto.PageableDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {
    public static Pageable createPageable(PageableDto pageableDto) {
        if (pageableDto.getProperty() == null) {
            return PageRequest.of(pageableDto.getPage(), pageableDto.getSize());
        } else {
            Sort sort = pageableDto.getDirection() == null
                    ? Sort.by(pageableDto.getProperty())
                    : Sort.by(pageableDto.getDirection(), pageableDto.getProperty());
            return PageRequest.of(pageableDto.getPage(), pageableDto.getSize(), sort);
        }
    }
}
