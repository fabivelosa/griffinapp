package com.callfailures.resource;


import java.time.LocalDateTime;
import java.util.List;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.callfailures.entity.Secured;
import com.callfailures.entity.views.DeviceCombination;
import com.callfailures.errors.ErrorMessage;
import com.callfailures.errors.ErrorMessages;
import com.callfailures.exception.InvalidDateException;
import com.callfailures.services.EventService;
import com.callfailures.utils.DateConverter;

@Path("/Combinations")
@Stateless
public class DeviceCommonEvents {

	//As a Network Management Engineer I want to see the Top 10 Market/Operator/Cell ID combinations
	//that had call failures during a time period 
	
	@EJB
	private  EventService eventService;
	
	
	
	@GET
	@Secured
    @Path("/query")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getTop10Combinations(@QueryParam("from") final Long fromEpoch,
			@QueryParam("to") final Long toEpoch) {
		
		final LocalDateTime startTime = DateConverter.convertLongToLocalDateTime(fromEpoch); 
		final LocalDateTime endTime = DateConverter.convertLongToLocalDateTime(toEpoch); 
		
		try {
			final List<DeviceCombination> topTen = eventService.findTopTenEvents(startTime, endTime);
			return Response.status(200).entity(topTen).build();
		} catch (InvalidDateException e) {
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_DATE.getMessage())).build();
		}
	}
	
	
	
}
