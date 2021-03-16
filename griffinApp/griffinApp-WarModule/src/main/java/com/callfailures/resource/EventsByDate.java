package com.callfailures.resource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.callfailures.services.EventService;
import com.callfailures.services.impl.EventServiceImpl;

@Path("/IMSIs")
@Stateless
public class EventsByDate {

	//As a Support Engineer I want to see a list of all IMSIs with call failures during a given time period
	
	@EJB
	private  EventService eventService = new EventServiceImpl();
	
	@GET
    @Path("/query")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIMSIsByDate(@QueryParam("from") final Long fromEpoch,
			@QueryParam("to") final Long toEpoch) {
	
		final LocalDateTime startTime = convertLongToLocalDateTime(fromEpoch); 
		final LocalDateTime endTime = convertLongToLocalDateTime(toEpoch); 
		List<String> imsis = eventService.findIMSISBetweenDates(startTime, endTime);
		return Response.status(400).build();
	}
	private LocalDateTime convertLongToLocalDateTime(final Long startEpoch) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(startEpoch), TimeZone.getDefault().toZoneId());
}
	
}
