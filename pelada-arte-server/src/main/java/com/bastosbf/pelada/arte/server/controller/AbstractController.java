package com.bastosbf.pelada.arte.server.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bastosbf.pelada.arte.server.dto.AbstractDto;
import com.bastosbf.pelada.arte.server.entity.AbstractEntity;
import com.bastosbf.pelada.arte.server.service.AbstractService;

public abstract class AbstractController<E extends AbstractEntity, D extends AbstractDto> {

	@RequestMapping("/{id}")
	@Transactional
	public D find(@PathVariable Long id) {
		return getService().findById(id);
	}

	public abstract AbstractService<E, D> getService();
}
