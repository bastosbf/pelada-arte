package com.bastosbf.pelada.arte.server.entity.impl;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bastosbf.pelada.arte.server.entity.AbstractEntity;

@Entity
@Table(name = "rate")
public class Rate extends AbstractEntity {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "pelada", nullable = false)
	private Pelada pelada;
	@Column(name = "date")
	private LocalDate date;
	@ManyToOne
	@JoinColumn(name = "rate_from", nullable = false)
	private Player rateFrom;
	@ManyToOne
	@JoinColumn(name = "rate_to", nullable = false)
	private Player rateTo;
	@Column(name = "rate", nullable = false)
	private Float rate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
