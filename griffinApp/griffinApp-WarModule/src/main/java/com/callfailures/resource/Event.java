package com.callfailures.resource;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.callfailures.dao.FailEventDAO;
import com.callfailures.entity.CallFailure;

@Path("/events") 
@Stateless
public class Event {

	@EJB
	private FailEventDAO failEvents; 

	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response saveEvent(CallFailure callFail) {
		failEvents.save(callFail);
		return Response.status(200).entity(callFail).build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes("application/json")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateWine(CallFailure callFail) {
		failEvents.update(callFail);
			return Response.status(200).entity(callFail).build();
		
	}

}
