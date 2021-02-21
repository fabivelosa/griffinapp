package com.callfailures.resource;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.callfailures.entity.CallFailure;
import com.callfailures.services.CallFailureService; 

@Path("/events") 
@Stateless
public class Event {

	@EJB
	private CallFailureService callfailureService; 

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public CallFailure getEvent(@PathParam("id") int eventId) {
		return callfailureService.findById(eventId);
	} 

}
