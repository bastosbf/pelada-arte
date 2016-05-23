package com.bastosbf.pelada.arte.server.operation;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.bastosbf.pelada.arte.server.HibernateConfig;
import com.bastosbf.pelada.arte.server.dao.PeladaDAO;
import com.bastosbf.pelada.arte.server.dao.PeladaPlayerDAO;
import com.bastosbf.pelada.arte.server.model.Pelada;
import com.bastosbf.pelada.arte.server.model.PeladaPlayer;
import com.bastosbf.pelada.arte.server.model.Player;

@Path("/pelada")
public class PeladaRESTOperation {

	@GET
	@Produces({ MediaType.TEXT_PLAIN})
	@Path("/add")
	public boolean add(@QueryParam("owner") String owner, @QueryParam("name") String name, @QueryParam("day") Integer day, @QueryParam("time") String time) {
		try {
			Player o = new Player();
			o.setEmail(owner);
			Pelada pelada = new Pelada();
			pelada.setOwner(o);
			pelada.setName(name);
			pelada.setDay(day);
			pelada.setTime(time);

			PeladaPlayer peladaPlayer = new PeladaPlayer();
			peladaPlayer.setPelada(pelada);
			peladaPlayer.setPlayer(o);

			{
				PeladaDAO dao = new PeladaDAO(HibernateConfig.factory);
				if (!dao.exists(owner, name)) {
					dao.add(pelada);
				}
			}
			{
				PeladaPlayerDAO dao = new PeladaPlayerDAO(HibernateConfig.factory);
				dao.add(peladaPlayer);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public List<Pelada> list(@QueryParam("player") String player) {
		try {
			PeladaDAO dao = new PeladaDAO(HibernateConfig.factory);
			return dao.listPeladas(player);
		} catch (Exception e) {
			return null;
		}
	}

	@GET
	@Produces({ MediaType.TEXT_PLAIN})
	@Path("/add-player")
	public boolean addPlayer(@QueryParam("owner") String owner, @QueryParam("pelada") Integer pelada, @QueryParam("player") String player) {
		try {
			PeladaPlayerDAO dao = new PeladaPlayerDAO(HibernateConfig.factory);
			dao.add(owner, pelada, player);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list-players")
	public List<Player> listPlayers(@QueryParam("pelada") Integer pelada) {
		try {
			PeladaDAO dao = new PeladaDAO(HibernateConfig.factory);
			return dao.listPlayers(pelada);
		} catch (Exception e) {
			return null;
		}
	}
}
