package com.callfailures.resource;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.callfailures.entity.Events;
import com.callfailures.services.EventService; 

@Path("/events") 
@Stateless
public class Event {

	@EJB
	private EventService eventService;
	
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Events getEvent(@PathParam("id") int eventId) {
		Events event = eventService.findById(eventId);
		System.out.println(event.getEventCause().getDescription());
		return eventService.findById(eventId);
	} 
	
}
