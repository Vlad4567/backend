package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.workperiod.WorkPeriodDto;
import com.example.beautybook.model.WorkPeriod;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface WorkPeriodMapper {
    WorkPeriod toModel(WorkPeriodDto workPeriodDto);

    WorkPeriodDto toDto(WorkPeriod workPeriod);
}
