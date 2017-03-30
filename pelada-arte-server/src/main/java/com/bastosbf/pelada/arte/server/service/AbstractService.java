package com.bastosbf.pelada.arte.server.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bastosbf.pelada.arte.server.dto.AbstractDto;
import com.bastosbf.pelada.arte.server.entity.AbstractEntity;
import com.bastosbf.pelada.arte.server.mapper.AbstractMapper;

public interface AbstractService<E extends AbstractEntity, D extends AbstractDto> {
	default D findById(Long id) {
		E entity = getRepository().findOne(id);
		return getMapper().entityToDto(entity);
	}

	JpaRepository<E, Long> getRepository();

	AbstractMapper<E, D> getMapper();
}
