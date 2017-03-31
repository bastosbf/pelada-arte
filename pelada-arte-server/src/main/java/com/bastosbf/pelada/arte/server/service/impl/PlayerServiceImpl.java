package com.bastosbf.pelada.arte.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.bastosbf.pelada.arte.server.dto.impl.PlayerDto;
import com.bastosbf.pelada.arte.server.entity.impl.Player;
import com.bastosbf.pelada.arte.server.mapper.AbstractMapper;
import com.bastosbf.pelada.arte.server.mapper.impl.PlayerMapper;
import com.bastosbf.pelada.arte.server.repository.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {
	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private PlayerMapper playerMapper;

	@Override
	public JpaRepository<Player, Long> getRepository() {
		return playerRepository;
	}

	@Override
	public AbstractMapper<Player, PlayerDto> getMapper() {
		return playerMapper;
	}

}
