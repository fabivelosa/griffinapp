package com.callfailures.CustomerServiceRep;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.callfailures.entity.views.IMSIEvent;
import com.callfailures.services.EventService;
import com.callfailures.services.impl.EventServiceImpl;

@Path("/failures")
@Stateless
public class QueryIMSI {

	@EJB
	private  final EventService eventService = new EventServiceImpl();
	
	@GET @Path("{imsi}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findEventsByIMISI(@PathParam("imsi") final String imsi) {
		final List<IMSIEvent> events = eventService.findFailuresByImsi(imsi);
		if (events !=null) {
			return Response.status(200).entity(events).build();
		}
		return Response.status(400).build();
	}
	
	
	
	
	
}
