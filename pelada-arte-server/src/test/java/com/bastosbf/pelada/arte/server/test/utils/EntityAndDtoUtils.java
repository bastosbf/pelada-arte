package com.bastosbf.pelada.arte.server.test.utils;

import java.time.LocalTime;
import java.util.UUID;

import com.bastosbf.pelada.arte.server.dto.impl.PeladaDto;
import com.bastosbf.pelada.arte.server.dto.impl.PlayerDto;
import com.bastosbf.pelada.arte.server.entity.impl.Pelada;
import com.bastosbf.pelada.arte.server.entity.impl.Player;

public abstract class EntityAndDtoUtils {

	public static Player createValidPlayer() {
		Player player = new Player();
		player.setUid(UUID.randomUUID().toString());
		player.setEmail("playert@email.com");
		player.setName("Player");

		return player;
	}

	public static PlayerDto createValidPlayerDto() {
		PlayerDto player = new PlayerDto();
		player.setUid(UUID.randomUUID().toString());
		player.setEmail("playert@email.com");
		player.setName("Player");

		return player;
	}

	public static Pelada createValidPelada(Player owner) {
		Pelada pelada = new Pelada();
		pelada.setDayOfTheWeek('D');
		pelada.setName("Pelada");
		pelada.setOwner(owner);
		pelada.setTime(LocalTime.now());

		return pelada;
	}

	public static PeladaDto createValidPeladaDto(Long owner) {
		PeladaDto pelada = new PeladaDto();
		pelada.setDayOfTheWeek('D');
		pelada.setName("Pelada");
		pelada.setOwner(owner);
		pelada.setTime(LocalTime.now());

		return pelada;
	}
}
