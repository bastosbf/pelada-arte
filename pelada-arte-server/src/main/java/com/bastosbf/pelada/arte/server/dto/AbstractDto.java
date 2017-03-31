package com.bastosbf.pelada.arte.server.dto;

import javax.validation.constraints.NotNull;

public abstract class AbstractDto {
	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
