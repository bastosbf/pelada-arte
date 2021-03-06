package com.bastosbf.pelada.arte.server.dto.impl;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.bastosbf.pelada.arte.server.dto.AbstractDto;

public class RateDto extends AbstractDto {
	@NotNull
	private Long pelada;
	@NotNull
	private LocalDate date;
	@NotNull
	private Long rateFrom;
	@NotNull
	private Long rateTo;
	@NotNull
	private Float rate;

	public Long getPelada() {
		return pelada;
	}

	public void setPelada(Long pelada) {
		this.pelada = pelada;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getRateFrom() {
		return rateFrom;
	}

	public void setRateFrom(Long rateFrom) {
		this.rateFrom = rateFrom;
	}

	public Long getRateTo() {
		return rateTo;
	}

	public void setRateTo(Long rateTo) {
		this.rateTo = rateTo;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}
}