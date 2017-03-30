package com.bastosbf.pelada.arte.server.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bastosbf.pelada.arte.server.controller.AbstractController;
import com.bastosbf.pelada.arte.server.dto.impl.RateDto;
import com.bastosbf.pelada.arte.server.entity.impl.Rate;
import com.bastosbf.pelada.arte.server.service.AbstractService;
import com.bastosbf.pelada.arte.server.service.impl.RateService;

@RestController
@RequestMapping("/rate")
public class RateController extends AbstractController<Rate, RateDto> {
	@Autowired
	private RateService rateService;

	@Override
	public AbstractService<Rate, RateDto> getService() {
		return rateService;
	}
}
