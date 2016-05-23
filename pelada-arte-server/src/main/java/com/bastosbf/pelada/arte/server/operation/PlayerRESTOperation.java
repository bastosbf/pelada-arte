package com.bastosbf.pelada.arte.server.operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.bastosbf.pelada.arte.server.HibernateConfig;
import com.bastosbf.pelada.arte.server.dao.PlayerDAO;
import com.bastosbf.pelada.arte.server.dao.RateDAO;
import com.bastosbf.pelada.arte.server.model.Player;

@Path("/player")
public class PlayerRESTOperation {

	@GET
	@Produces({ MediaType.TEXT_PLAIN})
	@Path("/add")
	public boolean add(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("name") String name) {
		try {
			Player player = new Player();
			player.setEmail(email);
			player.setPassword(password);
			player.setName(name);

			PlayerDAO dao = new PlayerDAO(HibernateConfig.factory);
			dao.add(player);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/login")
	public Player login(@QueryParam("email") String email, @QueryParam("password") String password) {
		try {
			PlayerDAO dao = new PlayerDAO(HibernateConfig.factory);
			return dao.login(email, password);
		} catch (Exception e) {
			return null;
		}
	}
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN})
	@Path("/rate")
	public boolean rate(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("pelada") int pelada, @QueryParam("rate") float rate) {
		try {
			RateDAO dao = new RateDAO(HibernateConfig.factory);
			dao.add(from, to, pelada, rate);
			return true; 
		} catch (Exception e) {
			return false;
		}
	}
}
