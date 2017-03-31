package com.bastosbf.pelada.arte.server.mapper.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.bastosbf.pelada.arte.server.dto.impl.RateDto;
import com.bastosbf.pelada.arte.server.entity.impl.Rate;
import com.bastosbf.pelada.arte.server.mapper.impl.RateMapper;
import com.bastosbf.pelada.arte.server.repository.PeladaRepository;
import com.bastosbf.pelada.arte.server.repository.PlayerRepository;

public abstract class RateMapperDecorator implements RateMapper {
	@Autowired
	@Qualifier("delegate")
	private RateMapper delegate;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private PeladaRepository peladaRepository;

	@Override
	public RateDto entityToDto(Rate source) {
		RateDto dto = delegate.entityToDto(source);
		if (dto != null) {
			dto.setPelada(source.getPelada().getId());
			dto.setRateFrom(source.getRateFrom().getId());
			dto.setRateTo(source.getRateTo().getId());
		}
		return dto;
	}

	@Override
	public Rate dtoToEntity(RateDto source) {
		Rate entity = delegate.dtoToEntity(source);
		if (entity != null) {
			entity.setPelada(peladaRepository.findOne(source.getPelada()));
			entity.setRateFrom(playerRepository.findOne(source.getRateFrom()));
			entity.setRateTo(playerRepository.findOne(source.getRateTo()));
		}
		return entity;
	}
}
