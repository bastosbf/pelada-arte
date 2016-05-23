package com.bastosbf.pelada.arte.server;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.bastosbf.pelada.arte.server.model.Pelada;
import com.bastosbf.pelada.arte.server.model.PeladaPlayer;
import com.bastosbf.pelada.arte.server.model.Player;
import com.bastosbf.pelada.arte.server.model.Rate;
import com.bastosbf.pelada.arte.server.operation.PeladaRESTOperation;
import com.bastosbf.pelada.arte.server.operation.PlayerRESTOperation;

public class HibernateConfig {

	public static final SessionFactory factory = new Configuration()
			.configure()
			.addClass(Player.class)
			.addClass(Pelada.class)
			.addClass(PeladaPlayer.class)
			.addClass(Rate.class)
			.buildSessionFactory();

	public static void main(String[] args) throws Exception {
		{
			PlayerRESTOperation op = new PlayerRESTOperation();
			System.out.println(op.add("didi@peladaarte.com", "123", "Didi"));
			System.out.println(op.add("fabu@peladaarte.com", "123", "Fabu"));
			System.out.println(op.login("didi@peladaarte.com", "123"));
			System.out.println(op.login("didi@peladaarte.com", "1234"));
		}

		{
			PeladaRESTOperation op = new PeladaRESTOperation();
			System.out.println(op.add("didi@peladaarte.com", "Ekipe", 5, "20:00"));
			List<Pelada> peladas = op.list("didi@peladaarte.com");
			System.out.println(peladas);
			System.out.println(op.addPlayer("didi@peladaarte.com", peladas.get(0).getId(), "fabu@peladaarte.com"));
			System.out.println(op.listPlayers(peladas.get(0).getId()));
		}

		System.exit(0);
	}
}
