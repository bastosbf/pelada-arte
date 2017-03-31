package com.bastosbf.pelada.arte.server.entity.impl;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.bastosbf.pelada.arte.server.entity.AbstractEntity;

@Entity
@Table(name = "player")
public class Player extends AbstractEntity {
	@NotNull
	@Column(name = "uid", unique = true, nullable = false)
	private String uid;
	@NotNull
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	@NotNull
	@Column(name = "name", nullable = false)
	private String name;
	@ManyToMany
	@JoinTable(name = "pelada_player", joinColumns = {
			@JoinColumn(name = "player", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "pelada", nullable = false, updatable = false) })
	private Set<Pelada> peladas;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Pelada> getPeladas() {
		return peladas;
	}

	public void setPeladas(Set<Pelada> peladas) {
		this.peladas = peladas;
	}
}
