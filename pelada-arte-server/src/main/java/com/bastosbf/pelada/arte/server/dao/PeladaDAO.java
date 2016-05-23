package com.bastosbf.pelada.arte.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bastosbf.pelada.arte.server.model.Pelada;
import com.bastosbf.pelada.arte.server.model.PeladaPlayer;
import com.bastosbf.pelada.arte.server.model.Player;

public class PeladaDAO extends GenericDAO<Pelada> {

	public PeladaDAO(SessionFactory factory) {
		super(factory);
	}

	public boolean exists(String owner, String name) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Pelada.class)
				.createAlias("owner", "pl")
				.add(Restrictions.eq("pl.email", owner))
				.add(Restrictions.eq("name", name));				
		List<Pelada> list = criteria.list();
		return !list.isEmpty();
	}
	
	public List<Player> listPlayers(Integer pelada) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(PeladaPlayer.class)
				.createAlias("pelada", "p")
        .createAlias("player", "pl")
				.add(Restrictions.eq("p.id", pelada))
				.addOrder(Order.asc("pl.name"));
		List<PeladaPlayer> list = criteria.list();
		List<Player> players = new ArrayList<Player>();
		if (!list.isEmpty()) {
			for (PeladaPlayer peladaPlayer : list) {
				players.add(peladaPlayer.getPlayer());
			}
		}
		return players;
	}
	
	public List<Pelada> listPeladas(String player) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(PeladaPlayer.class)
				.createAlias("pelada", "p")
        .createAlias("player", "pl")
				.add(Restrictions.eq("pl.email", player))
				.addOrder(Order.asc("p.name"));
		List<PeladaPlayer> list = criteria.list();
		List<Pelada> pelada = new ArrayList<Pelada>();
		if (!list.isEmpty()) {
			for (PeladaPlayer peladaPlayer : list) {
				pelada.add(peladaPlayer.getPelada());
			}
		}
		return pelada;
	}
}
