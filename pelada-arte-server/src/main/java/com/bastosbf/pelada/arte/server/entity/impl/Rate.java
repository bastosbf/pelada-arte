package com.bastosbf.pelada.arte.server.entity.impl;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.bastosbf.pelada.arte.server.entity.AbstractEntity;

@Entity
@Table(name = "rate")
public class Rate extends AbstractEntity {
	@NotNull
	@ManyToOne
	@JoinColumn(name = "pelada", nullable = false)
	private Pelada pelada;
	@NotNull
	@Column(name = "date")
	private LocalDate date;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "rate_from", nullable = false)
	private Player rateFrom;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "rate_to", nullable = false)
	private Player rateTo;
	@NotNull
	@Column(name = "rate", nullable = false)
	private Float rate;

	public Pelada getPelada() {
		return pelada;
	}

	public void setPelada(Pelada pelada) {
		this.pelada = pelada;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Player getRateFrom() {
		return rateFrom;
	}

	public void setRateFrom(Player rateFrom) {
		this.rateFrom = rateFrom;
	}

	public Player getRateTo() {
		return rateTo;
	}

	public void setRateTo(Player rateTo) {
		this.rateTo = rateTo;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}
}
