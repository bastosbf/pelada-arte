package com.bastosbf.pelada.arte.server.entity.impl;

import java.time.LocalTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bastosbf.pelada.arte.server.entity.AbstractEntity;

@Entity
@Table(name = "pelada")
public class Pelada extends AbstractEntity {
	@ManyToOne
	@JoinColumn(name = "owner", nullable = false)
	private Player owner;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "day_of_the_week", nullable = false)
	private Character dayOfTheWeek;
	@Column(name = "time", nullable = false)
	private LocalTime time;
	@ManyToMany(mappedBy = "peladas")
	private Set<Player> players;

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
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

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
}
