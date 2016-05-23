package com.bastosbf.pelada.arte.server.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PeladaPlayer implements Serializable {
	private Player player;
	private Pelada pelada;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Pelada getPelada() {
		return pelada;
	}

	public void setPelada(Pelada pelada) {
		this.pelada = pelada;
	}

}
