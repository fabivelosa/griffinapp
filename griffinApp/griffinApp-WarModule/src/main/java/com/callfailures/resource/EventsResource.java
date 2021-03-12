package com.callfailures.resource;

import java.time.DateTimeException;
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

import com.callfailures.entity.views.IMSISummary;
import com.callfailures.entity.views.PhoneFailures;
import com.callfailures.errors.ErrorMessage;
import com.callfailures.errors.ErrorMessages;
import com.callfailures.exception.InvalidDateException;
import com.callfailures.exception.InvalidIMSIException;
import com.callfailures.services.EventService;

@Path("/events")
@Stateless
public class EventsResource {

	@EJB
	private EventService eventService;
	
	/**
	 * Network Engineer: Count call failures for a given IMSI during a certain period
	 * @param imsi - the IMSI parameter
	 * @param fromEpoch - the starting Date parameter converted to long or UNIX timestamp
	 * @param toEpoch - the starting Date parameter converted to long or UNIX timestamp
	 * @param summary - boolean parameter which checks if summary is required or not
	 * @return Returns IMSISummary entity which contains the (1) total failure count and (2) total duration of failues 
	 */
	@GET
    @Path("/query")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIMSICallFailureSummary(
			@QueryParam("imsi") final String imsi,
			@QueryParam("from") final Long fromEpoch,
			@QueryParam("to") final Long toEpoch,
			@QueryParam("summary") final boolean summary
			) {
		
		final LocalDateTime startTime = convertLongToLocalDateTime(fromEpoch); 
		final LocalDateTime endTime = convertLongToLocalDateTime(toEpoch); 	
		try {	
			IMSISummary imsiSummary = eventService.findCallFailuresCountByIMSIAndDate(imsi, startTime, endTime);
			if(imsiSummary == null) {
				imsiSummary = new IMSISummary(imsi, 0, 0);
			}
			return Response.status(200).entity(imsiSummary).build();
		}catch(InvalidIMSIException exception) {
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_IMSI.getMessage())).build();
		}catch(InvalidDateException exception) {
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_DATE.getMessage())).build();
		}
	}

	private LocalDateTime convertLongToLocalDateTime(final Long startEpoch) {
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(startEpoch), TimeZone.getDefault().toZoneId());
	}
	
	@GET
    @Path("/query")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findUniqueEventCauseCountByPhoneModel(
			@QueryParam("tac") final int tac
			) {
		List<PhoneFailures> phoneFailures =  eventService.findUniqueEventCauseCountByPhoneModel(tac);
		return Response.status(200).entity(phoneFailures).build();
	}
	
}
