package com.bastosbf.pelada.arte.server.dto.impl;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.bastosbf.pelada.arte.server.dto.AbstractDto;

public class PlayerDto extends AbstractDto {
	@NotNull
	private String uid;
	@NotNull
	private String email;
	@NotNull
	private String name;
	private Set<Long> peladas;

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

	public Set<Long> getPeladas() {
		return peladas;
	}

	public void setPeladas(Set<Long> peladas) {
		this.peladas = peladas;
	}
}
