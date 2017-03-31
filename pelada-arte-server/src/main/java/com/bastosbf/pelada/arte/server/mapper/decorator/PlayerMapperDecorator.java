package com.bastosbf.pelada.arte.server.mapper.decorator;

import static java.util.stream.Collectors.toSet;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.bastosbf.pelada.arte.server.dto.impl.PlayerDto;
import com.bastosbf.pelada.arte.server.entity.impl.Pelada;
import com.bastosbf.pelada.arte.server.entity.impl.Player;
import com.bastosbf.pelada.arte.server.mapper.impl.PlayerMapper;
import com.bastosbf.pelada.arte.server.repository.PeladaRepository;

public abstract class PlayerMapperDecorator implements PlayerMapper {
	@Autowired
	@Qualifier("delegate")
	private PlayerMapper delegate;

	@Autowired
	private PeladaRepository peladaRepository;

	@Override
	public PlayerDto entityToDto(Player source) {
		PlayerDto dto = delegate.entityToDto(source);
		if (dto != null) {
			dto.setPeladas(source.getPeladas().stream().map(Pelada::getId).collect(toSet()));
		}
		return dto;
	}

	@Override
	public Player dtoToEntity(PlayerDto source) {
		Player entity = delegate.dtoToEntity(source);
		if (entity != null) {
			entity.setPeladas(new HashSet<>(peladaRepository.findAll(source.getPeladas())));
		}
		return entity;
	}
}
