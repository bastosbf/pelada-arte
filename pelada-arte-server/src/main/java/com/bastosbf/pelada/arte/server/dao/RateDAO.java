package com.bastosbf.pelada.arte.server.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.bastosbf.pelada.arte.server.model.Pelada;
import com.bastosbf.pelada.arte.server.model.Player;
import com.bastosbf.pelada.arte.server.model.Rate;

public class RateDAO extends GenericDAO<Rate> {

	public RateDAO(SessionFactory factory) {
		super(factory);
	}

	public void add(String from, String to, int pelada, float rate) {
		Session session = factory.openSession();
		session.beginTransaction();
		Pelada p = null;
		Player rateFrom = null;
		Player rateTo = null;
		{
			Criteria criteria = session.createCriteria(Pelada.class)
					.add(Restrictions.eq("id", pelada));
			List<Pelada> list = criteria.list();
			if (!list.isEmpty()) {
				p = list.get(0);
			}
		}
		{
			Criteria criteria = session.createCriteria(Player.class)
					.add(Restrictions.eq("email", from));
			List<Player> list = criteria.list();
			if (!list.isEmpty()) {
				rateFrom = list.get(0);
			}
		}
		{
			Criteria criteria = session.createCriteria(Player.class)
					.add(Restrictions.eq("email", to));
			List<Player> list = criteria.list();
			if (!list.isEmpty()) {
				rateTo = list.get(0);
			}
		}
		Rate r = new Rate();
		r.setPelada(p);
		r.setRate_from(rateFrom);
		r.setRate_to(rateTo);
		r.setDate(new Date());
		r.setRate(rate);
		add(r);
	}

	public List<Rate> listLast(int pelada) {
		Session session = factory.openSession();
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT MAX(date) FROM rate WHERE pelada =" + pelada);
		List<Date> dates = query.list();
		if (dates.isEmpty()) {
			return listAll(pelada);
		}
		Criteria criteria = session.createCriteria(Rate.class)
				.createAlias("pelada", "p")
				.add(Restrictions.eq("p.id", pelada))
				.add(Restrictions.eq("date", dates.get(0)));
		return criteria.list();
	}

	public List<Rate> listAll(int pelada) {
		Session session = factory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Rate.class)
				.createAlias("pelada", "p")
				.add(Restrictions.eq("p.id", pelada));
		return criteria.list();
	}
}
