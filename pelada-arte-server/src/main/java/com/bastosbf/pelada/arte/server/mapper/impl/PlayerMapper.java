package com.bastosbf.pelada.arte.server.mapper.impl;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bastosbf.pelada.arte.server.dto.impl.PlayerDto;
import com.bastosbf.pelada.arte.server.entity.impl.Player;
import com.bastosbf.pelada.arte.server.mapper.AbstractMapper;
import com.bastosbf.pelada.arte.server.mapper.decorator.PlayerMapperDecorator;

@Mapper(componentModel = "spring")
@DecoratedWith(PlayerMapperDecorator.class)
public interface PlayerMapper extends AbstractMapper<Player, PlayerDto> {
	PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

	@Override
	@Mapping(target = "peladas", ignore = true)
	PlayerDto entityToDto(Player source);

	@Override
	@Mapping(target = "peladas", ignore = true)
	Player dtoToEntity(PlayerDto source);
}
