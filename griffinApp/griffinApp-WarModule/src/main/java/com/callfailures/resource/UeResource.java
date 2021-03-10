package com.callfailures.resource;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.callfailures.entity.views.PhoneModelSummary;
import com.callfailures.services.EventService;

@Path("/events")
@Stateless
public class UeResource {

	@EJB
	private EventService eventService;
	
	
	
	
	
	
	/**
	 * Support Engineer: Count call failures for a given IMSI during a certain period
	 * @param imsi - the IMSI parameter
	 * @param fromEpoch - the starting Date paramater converted to long or UNIX timestamp
	 * @param toEpoch - the starting Date parameter converted to long or UNIX timestamp
	 * @param summary - boolean parameter which checks if summary is required or not
	 * @return Returns IMSISummary entity which contains the (1) total failure count and (2) total duration of failues 
	 */
	@GET
    @Path("/query")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIMSICallFailureSummary(
			@QueryParam("model") final String model,
			@QueryParam("from") final Long fromEpoch,
			@QueryParam("to") final Long toEpoch
			//@QueryParam("summary") final boolean summary
			) {
		
		final LocalDateTime startTime = convertLongToLocalDateTime(fromEpoch); 
		final LocalDateTime endTime = convertLongToLocalDateTime(toEpoch); 	
		try {	
			PhoneModelSummary phoneModelSummary = eventService.findCallFailuresCountByPhoneModelAndDate(model, startTime, endTime);
			if(phoneModelSummary == null) {
				phoneModelSummary = new PhoneModelSummary (model, 0);
			}
			return Response.status(200).entity(phoneModelSummary).build();
		}catch(Exception exception) {
			return Response.status(404).build();
//		}catch(InvalidDateException exception) {
//			System.out.println("Exception is caught");
//			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_DATE.getMessage())).build();
//		}
	}
	
	}

	private LocalDateTime convertLongToLocalDateTime(final Long startEpoch) {
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(startEpoch), TimeZone.getDefault().toZoneId());
	}
	
}

