package com.callfailures.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.callfailures.entity.CallFailure;
import com.callfailures.services.CallFailureService; 

@Path("/events") 
public class Event {

	@Inject
	private CallFailureService callfailureService; 

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public CallFailure getEvent(@PathParam("id") int eventId) {
		return callfailureService.findById(eventId);
	} 

}
