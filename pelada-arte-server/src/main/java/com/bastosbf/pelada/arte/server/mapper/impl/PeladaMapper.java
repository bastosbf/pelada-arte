package com.bastosbf.pelada.arte.server.mapper.impl;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.bastosbf.pelada.arte.server.dto.impl.PeladaDto;
import com.bastosbf.pelada.arte.server.entity.impl.Pelada;
import com.bastosbf.pelada.arte.server.mapper.AbstractMapper;
import com.bastosbf.pelada.arte.server.mapper.decorator.PeladaMapperDecorator;

@Mapper(componentModel = "spring")
@DecoratedWith(PeladaMapperDecorator.class)
public interface PeladaMapper extends AbstractMapper<Pelada, PeladaDto> {
	@Override
	@Mappings({ @Mapping(target = "players", ignore = true), @Mapping(target = "owner", ignore = true) })
	PeladaDto entityToDto(Pelada source);

	@Override
	@Mappings({ @Mapping(target = "players", ignore = true), @Mapping(target = "owner", ignore = true) })
	Pelada dtoToEntity(PeladaDto source);
}
