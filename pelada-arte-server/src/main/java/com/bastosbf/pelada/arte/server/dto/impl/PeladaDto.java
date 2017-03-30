package com.bastosbf.pelada.arte.server.dto.impl;

import java.time.LocalTime;
import java.util.Set;

import com.bastosbf.pelada.arte.server.dto.AbstractDto;

public class PeladaDto extends AbstractDto {
	private Long owner;
	private String name;
	private Character dayOfTheWeek;
	private LocalTime time;
	private Set<Long> players;

	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getDayOfTheWeek() {
		return dayOfTheWeek;
	}

	public void setDayOfTheWeek(Character dayOfTheWeek) {
		this.dayOfTheWeek = dayOfTheWeek;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Set<Long> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Long> players) {
		this.players = players;
	}
}
