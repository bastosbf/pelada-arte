package com.bastosbf.pelada.arte.server.operation;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.bastosbf.pelada.arte.server.HibernateConfig;
import com.bastosbf.pelada.arte.server.dao.RateDAO;
import com.bastosbf.pelada.arte.server.model.Rate;

@Path("/rate")
public class RateRESTOperation {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list-previous")
	public List<Rate> listPrevious(@QueryParam("pelada") int pelada) {
		try {
			RateDAO dao = new RateDAO(HibernateConfig.factory);
			return dao.listLast(pelada);
		} catch (Exception e) {
			return null;
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list-global")
	public List<Rate> listGlobal(@QueryParam("pelada") int pelada) {
		try {
			RateDAO dao = new RateDAO(HibernateConfig.factory);
			return dao.listAll(pelada);
		} catch (Exception e) {
			return null;
		}
	}	
}
