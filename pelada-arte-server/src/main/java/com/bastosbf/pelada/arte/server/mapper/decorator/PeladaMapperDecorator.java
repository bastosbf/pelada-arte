package com.bastosbf.pelada.arte.server.mapper.decorator;

import static java.util.stream.Collectors.toSet;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.bastosbf.pelada.arte.server.dto.impl.PeladaDto;
import com.bastosbf.pelada.arte.server.entity.impl.Pelada;
import com.bastosbf.pelada.arte.server.entity.impl.Player;
import com.bastosbf.pelada.arte.server.mapper.impl.PeladaMapper;
import com.bastosbf.pelada.arte.server.repository.PlayerRepository;

public abstract class PeladaMapperDecorator implements PeladaMapper {
	@Autowired
	@Qualifier("delegate")
	private PeladaMapper delegate;

	@Autowired
	private PlayerRepository playerRepository;

	@Override
	public PeladaDto entityToDto(Pelada source) {
		PeladaDto dto = delegate.entityToDto(source);
		dto.setOwner(source.getOwner().getId());
		dto.setPlayers(source.getPlayers().stream().map(Player::getId).collect(toSet()));
		return dto;
	}

	@Override
	public Pelada dtoToEntity(PeladaDto source) {
		Pelada entity = delegate.dtoToEntity(source);
		entity.setPlayers(new HashSet<>(playerRepository.findAll(source.getPlayers())));
		return entity;
	}
}
