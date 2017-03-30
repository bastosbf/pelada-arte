package com.bastosbf.pelada.arte.server.entity.impl;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.bastosbf.pelada.arte.server.entity.AbstractEntity;

@Entity
@Table(name = "player")
public class Player extends AbstractEntity {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Column(name = "uid", unique = true, nullable = false)
	private String uid;
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	@Column(name = "name", nullable = false)
	private String name;
	@ManyToMany
	@JoinTable(name = "pelada_player", joinColumns = {
			@JoinColumn(name = "player", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "pelada", nullable = false, updatable = false) })
	private Set<Pelada> peladas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
