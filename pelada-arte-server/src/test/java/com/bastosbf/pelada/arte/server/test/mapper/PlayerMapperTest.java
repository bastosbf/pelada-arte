package com.bastosbf.pelada.arte.server.test.mapper;

import static com.bastosbf.pelada.arte.server.test.utils.EntityAndDtoUtils.createValidPelada;
import static com.bastosbf.pelada.arte.server.test.utils.EntityAndDtoUtils.createValidPlayer;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import java.lang.reflect.Field;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.bastosbf.pelada.arte.server.dto.impl.PlayerDto;
import com.bastosbf.pelada.arte.server.entity.impl.Pelada;
import com.bastosbf.pelada.arte.server.entity.impl.Player;
import com.bastosbf.pelada.arte.server.mapper.decorator.PlayerMapperDecorator;
import com.bastosbf.pelada.arte.server.mapper.impl.PlayerMapper;
import com.bastosbf.pelada.arte.server.repository.PeladaRepository;

@RunWith(MockitoJUnitRunner.class)
public class PlayerMapperTest {
	private static final Long OWNER_ID = 1L;
	private static final Long PLAYER_ID = 10L;
	private static final Long PELADA_ID = 100L;

	@Mock
	private PeladaRepository peladaRepository;
	@Mock
	private PlayerMapper delegate;

	private PlayerMapper playerMapper;

	@Before
	public void configure() throws Exception {
		playerMapper = Mappers.getMapper(PlayerMapper.class);

		// hack for mapstruct unit tests
		Field delegateField = PlayerMapperDecorator.class.getDeclaredField("delegate");
		delegateField.setAccessible(true);
		Class<?> clazz = Class.forName(PlayerMapper.class.getName() + "Impl_");
		delegateField.set(playerMapper, clazz.newInstance());

		Field peladaRepositoryField = PlayerMapperDecorator.class.getDeclaredField("peladaRepository");
		peladaRepositoryField.setAccessible(true);
		peladaRepositoryField.set(playerMapper, peladaRepository);
	}

	@Test
	public void testPlayerMapperEntityToDto() {
		Player owner = createValidPlayer();
		owner.setId(OWNER_ID);
		Player player = createValidPlayer();
		player.setId(PLAYER_ID);
		Pelada pelada = createValidPelada(owner);
		pelada.setId(PELADA_ID);
		player.setPeladas(new HashSet<>(asList(pelada)));

		given(peladaRepository.findAll(asList(PELADA_ID))).willReturn(asList(pelada));

		PlayerDto playerDto = playerMapper.entityToDto(player);
		assertEquals(playerDto.getId(), player.getId());
		assertEquals(playerDto.getUid(), player.getUid());
		assertEquals(playerDto.getEmail(), player.getEmail());
		assertEquals(playerDto.getName(), player.getName());
		assertEquals(playerDto.getPeladas().iterator().next(), player.getPeladas().iterator().next().getId());
	}
}
