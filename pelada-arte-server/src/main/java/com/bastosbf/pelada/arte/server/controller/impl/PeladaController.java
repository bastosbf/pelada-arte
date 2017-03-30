package com.bastosbf.pelada.arte.server.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bastosbf.pelada.arte.server.controller.AbstractController;
import com.bastosbf.pelada.arte.server.dto.impl.PeladaDto;
import com.bastosbf.pelada.arte.server.entity.impl.Pelada;
import com.bastosbf.pelada.arte.server.service.AbstractService;
import com.bastosbf.pelada.arte.server.service.impl.PeladaService;

@RestController
@RequestMapping("/pelada")
public class PeladaController extends AbstractController<Pelada, PeladaDto> {
	@Autowired
	private PeladaService peladaService;

	@Override
	public AbstractService<Pelada, PeladaDto> getService() {
		return peladaService;
	}
}
