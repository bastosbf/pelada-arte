package com.bastosbf.pelada.arte.server.mapper;

import com.bastosbf.pelada.arte.server.dto.AbstractDto;
import com.bastosbf.pelada.arte.server.entity.AbstractEntity;

public interface AbstractMapper<E extends AbstractEntity, D extends AbstractDto> {
	E dtoToEntity(D source);

	D entityToDto(E source);
}
