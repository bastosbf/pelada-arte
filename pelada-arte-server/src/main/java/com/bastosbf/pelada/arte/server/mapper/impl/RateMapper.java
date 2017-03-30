package com.bastosbf.pelada.arte.server.mapper.impl;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.bastosbf.pelada.arte.server.dto.impl.RateDto;
import com.bastosbf.pelada.arte.server.entity.impl.Rate;
import com.bastosbf.pelada.arte.server.mapper.AbstractMapper;
import com.bastosbf.pelada.arte.server.mapper.decorator.RateMapperDecorator;

@Mapper(componentModel = "spring")
@DecoratedWith(RateMapperDecorator.class)
public interface RateMapper extends AbstractMapper<Rate, RateDto> {
	@Override
	@Mappings({ @Mapping(target = "pelada", ignore = true), @Mapping(target = "rateFrom", ignore = true),
			@Mapping(target = "rateTo", ignore = true) })
	RateDto entityToDto(Rate source);

	@Override
	@Mappings({ @Mapping(target = "pelada", ignore = true), @Mapping(target = "rateFrom", ignore = true),
			@Mapping(target = "rateTo", ignore = true) })
	Rate dtoToEntity(RateDto source);
}
