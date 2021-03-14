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
import com.callfailures.errors.ErrorMessage;
import com.callfailures.errors.ErrorMessages;
import com.callfailures.exception.InvalidIMSIException;
import com.callfailures.services.EventService;
import com.callfailures.services.impl.EventServiceImpl;

@Path("/failures")
@Stateless
public class QueryIMSI {

	@EJB
	private  EventService eventService = new EventServiceImpl();
	
	@GET @Path("{imsi}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findEventsByIMISI(@PathParam("imsi") String imsi) {
		
		try {
			List<IMSIEvent> events = eventService.findFailuresByImsi(imsi);
			if (events == null) {
				throw new InvalidIMSIException();
			}
			return Response.status(200).entity(events).build();
		} catch (InvalidIMSIException exception) {
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_IMSI.getMessage())).build();
		}
	}
	
	
	
	
	
}
