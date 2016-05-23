package com.bastosbf.pelada.arte.server.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.bastosbf.pelada.arte.server.model.Player;

public class PlayerDAO extends GenericDAO<Player> {

	public PlayerDAO(SessionFactory factory) {
		super(factory);
	}

	public Player login(String email, String password) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Player.class)
				.add(Restrictions.eq("email", email))
				.add(Restrictions.eq("password", password));
		List<Player> list = criteria.list();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
}
