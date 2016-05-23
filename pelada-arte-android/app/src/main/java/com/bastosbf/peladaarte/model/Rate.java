package com.bastosbf.peladaarte.model;

import java.io.Serializable;
import java.util.Date;

public class Rate implements Serializable {
	private Pelada pelada;
	private Date date;
	private Player rate_from;
	private Player rate_to;
	private float rate;

	public Pelada getPelada() {
		return pelada;
	}

	public void setPelada(Pelada pelada) {
		this.pelada = pelada;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Player getRate_from() {
		return rate_from;
	}

	public void setRate_from(Player rate_from) {
		this.rate_from = rate_from;
	}

	public Player getRate_to() {
		return rate_to;
	}

	public void setRate_to(Player rate_to) {
		this.rate_to = rate_to;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

}
