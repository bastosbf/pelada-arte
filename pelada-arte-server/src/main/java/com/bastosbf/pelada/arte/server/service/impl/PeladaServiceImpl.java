package com.bastosbf.pelada.arte.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.bastosbf.pelada.arte.server.dto.impl.PeladaDto;
import com.bastosbf.pelada.arte.server.entity.impl.Pelada;
import com.bastosbf.pelada.arte.server.mapper.AbstractMapper;
import com.bastosbf.pelada.arte.server.mapper.impl.PeladaMapper;
import com.bastosbf.pelada.arte.server.repository.PeladaRepository;

@Service
public class PeladaServiceImpl implements PeladaService {
	@Autowired
	private PeladaRepository peladaRepository;

	@Autowired
	private PeladaMapper peladaMapper;

	@Override
	public JpaRepository<Pelada, Long> getRepository() {
		return peladaRepository;
	}

	@Override
	public AbstractMapper<Pelada, PeladaDto> getMapper() {
		return peladaMapper;
	}

}
