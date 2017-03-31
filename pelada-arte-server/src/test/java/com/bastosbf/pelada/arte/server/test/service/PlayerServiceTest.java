package com.bastosbf.pelada.arte.server.test.service;

import static com.bastosbf.pelada.arte.server.test.utils.EntityAndDtoUtils.createValidPlayer;
import static com.bastosbf.pelada.arte.server.test.utils.EntityAndDtoUtils.createValidPlayerDto;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.bastosbf.pelada.arte.server.dto.impl.PlayerDto;
import com.bastosbf.pelada.arte.server.entity.impl.Player;
import com.bastosbf.pelada.arte.server.mapper.impl.PlayerMapper;
import com.bastosbf.pelada.arte.server.repository.PlayerRepository;
import com.bastosbf.pelada.arte.server.service.impl.PlayerServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {
	private static final Long PLAYER_ID = 1L;

	@Mock
	private PlayerRepository playerRepository;
	@Mock
	private PlayerMapper playerMapper;

	@InjectMocks
	private PlayerServiceImpl playerService;

	@Test
	public void testFindByID() {
		Player player = createValidPlayer();
		player.setId(PLAYER_ID);
		PlayerDto playerDto = createValidPlayerDto();
		playerDto.setId(PLAYER_ID);

		given(playerRepository.findOne(PLAYER_ID)).willReturn(player);
		given(playerMapper.entityToDto(player)).willReturn(playerDto);

		PlayerDto result = playerService.findById(PLAYER_ID);
		assertEquals(result, playerDto);
	}
}
