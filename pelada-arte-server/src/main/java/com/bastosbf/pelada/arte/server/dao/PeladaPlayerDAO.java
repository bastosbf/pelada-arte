package com.bastosbf.pelada.arte.server.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.bastosbf.pelada.arte.server.model.Pelada;
import com.bastosbf.pelada.arte.server.model.PeladaPlayer;
import com.bastosbf.pelada.arte.server.model.Player;

public class PeladaPlayerDAO extends GenericDAO<PeladaPlayer> {

	public PeladaPlayerDAO(SessionFactory factory) {
		super(factory);
	}

	public void add(String owner, Integer pelada, String player) {
		Session session = factory.openSession();
		session.beginTransaction();
		Pelada p = null;
		Player pl = null;
		{
			Criteria criteria = session.createCriteria(Pelada.class)
					.createAlias("owner", "pl")
					.add(Restrictions.eq("pl.email", owner))
					.add(Restrictions.eq("id", pelada));
			List<Pelada> list = criteria.list();
			if (!list.isEmpty()) {
				p = list.get(0);
			}
		}
		{
			Criteria criteria = session.createCriteria(Player.class)
					.add(Restrictions.eq("email", player));
			List<Player> list = criteria.list();
			if (!list.isEmpty()) {
				pl = list.get(0);
			}
		}
		PeladaPlayer peladaPlayer = new PeladaPlayer();
		peladaPlayer.setPelada(p);
		peladaPlayer.setPlayer(pl);
		add(peladaPlayer);
	}
}
