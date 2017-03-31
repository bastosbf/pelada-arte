package com.bastosbf.pelada.arte.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.bastosbf.pelada.arte.server.dto.impl.RateDto;
import com.bastosbf.pelada.arte.server.entity.impl.Rate;
import com.bastosbf.pelada.arte.server.mapper.AbstractMapper;
import com.bastosbf.pelada.arte.server.mapper.impl.RateMapper;
import com.bastosbf.pelada.arte.server.repository.RateRepository;

@Service
public class RateServiceImpl implements RateService {
	@Autowired
	private RateRepository rateRepository;

	@Autowired
	private RateMapper rateMapper;

	@Override
	public JpaRepository<Rate, Long> getRepository() {
		return rateRepository;
	}

	@Override
	public AbstractMapper<Rate, RateDto> getMapper() {
		return rateMapper;
	}

}
