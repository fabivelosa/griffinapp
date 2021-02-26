package com.callfailures.resource;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.callfailures.dto.EventsUploadResponseDTO;
import com.callfailures.entity.Events;
import com.callfailures.errors.DataErrorMessage;
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
	

	
	@GET
	@Path("/sampleJSON2")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSampleJSONResponse() {
		EventsUploadResponseDTO sample =  new EventsUploadResponseDTO();
		
		for(int i = 0; i < 30000; i++) {
			sample.addValidDataRowNumber(i);
		}
		
		sample.addErroneousData(new DataErrorMessage(1, "Missing IMSI"));
		sample.addErroneousData(new DataErrorMessage(2, "Invalid MCC-MNC Combination"));

		return Response.status(200).entity(sample).build();
	}
	
}
