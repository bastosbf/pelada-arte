package com.bastosbf.pelada.arte.server.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bastosbf.pelada.arte.server.controller.AbstractController;
import com.bastosbf.pelada.arte.server.dto.impl.PlayerDto;
import com.bastosbf.pelada.arte.server.entity.impl.Player;
import com.bastosbf.pelada.arte.server.service.AbstractService;
import com.bastosbf.pelada.arte.server.service.impl.PlayerService;

@RestController
@RequestMapping("/player")
public class PlayerController extends AbstractController<Player, PlayerDto> {
	@Autowired
	private PlayerService playerService;

	@Override
	public AbstractService<Player, PlayerDto> getService() {
		return playerService;
	}
}
