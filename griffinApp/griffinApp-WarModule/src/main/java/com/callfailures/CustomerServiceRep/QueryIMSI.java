package com.callfailures.CustomerServiceRep;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.callfailures.entity.views.IMSIEvent;
import com.callfailures.services.EventService;
import com.callfailures.services.impl.EventServiceImpl;


@Path("/failures")
@Stateless
public class QueryIMSI {

	/* 
	 * As Customer Service Rep. 
	I want to display, for a given affected IMSI,
	the Event ID and Cause Code for any / all failures affecting that IMSI
	So that I can confirm the failure reported by the customer
	*/
	
	@EJB
	private  EventService eventService = new EventServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response findEventsByIMISI() {
		System.out.println("Called");
		
		List<IMSIEvent> events = eventService.findFailuresByImsi("344930000000011");
		if (events !=null) {
			return Response.status(200).entity(events).build();
		}
		return Response.status(400).build();
		
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("search/IMSI")
	public Response findEventsByIMISI(@PathParam("IMSI") String IMSI) {
		System.out.println("Called");
		List<IMSIEvent> events = eventService.findFailuresByImsi(IMSI);
		if (events !=null) {
			return Response.status(200).build();
		}
		return Response.status(400).build();
	}
	
	
	
	
	
}
