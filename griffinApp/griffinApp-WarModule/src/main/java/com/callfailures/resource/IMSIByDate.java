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

import com.callfailures.entity.Secured;
import com.callfailures.entity.views.IMSICount;
import com.callfailures.entity.views.UniqueIMSI;
import com.callfailures.errors.ErrorMessage;
import com.callfailures.errors.ErrorMessages;
import com.callfailures.exception.InvalidDateException;
import com.callfailures.services.EventService;

@Path("/IMSIs")
@Stateless
public class IMSIByDate {

	//As a Support Engineer I want to see a list of all IMSIs with call failures during a given time period
	//http://localhost:8080/callfailures/api/IMSIs/query?from=1578762900000&to=1578763800000
	@EJB
	private  EventService eventService;
	
	/**
	 * Support Engineer: Retreive List of IMSIs with failures during a given time period
	 * @param fromEpoch - the starting Date parameter converted to long or UNIX timestamp
	 * @param toEpoch - the starting Date parameter converted to long or UNIX timestamp
	 * @return Returns List of IMSI names
	 */
	@GET
	@Secured
    @Path("/query")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIMSIsByDate(
			@QueryParam("from") final Long fromEpoch,
			@QueryParam("to") final Long toEpoch) {
	
		final LocalDateTime startTime = convertLongToLocalDateTime(fromEpoch); 
		final LocalDateTime endTime = convertLongToLocalDateTime(toEpoch); 
		try {
			final List<UniqueIMSI> imsis = eventService.findIMSISBetweenDates(startTime, endTime);
			return Response.status(200).entity(imsis).build();
		} catch (InvalidDateException exception) {
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_DATE.getMessage())).build();
		}		
	}
	
	
	/**
	 * Support Engineer: Retreive List of IMSIs with failures 
	 * @return Returns List of IMSI names
	 */
	@GET
	@Secured
    @Path("/query/all")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIMSIs() {
		try {
			final List<UniqueIMSI> imsis = eventService.findIMSIS();
			return Response.status(200).entity(imsis).build();
		} catch (Exception exception) {
			return Response.status(404).build();
		}		
	}
	
	/**
	 * Support Engineer: Retrieve top N IMSIs with failures 
	 * @return Returns List of IMSI names
	 */
	@GET
	@Secured
    @Path("/query/limit")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getTopIMSIs(@QueryParam("number") final int number,
			@QueryParam("from") final Long fromEpoch,
			@QueryParam("to") final Long toEpoch) {
		
		final LocalDateTime startTime = convertLongToLocalDateTime(fromEpoch); 
		final LocalDateTime endTime = convertLongToLocalDateTime(toEpoch); 
		
		try {
			final List<IMSICount> imsis = eventService.findIMSIS(number, startTime, endTime);
			return Response.status(200).entity(imsis).build();
		} 
		catch (InvalidDateException exception) {
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_DATE.getMessage())).build();
		}
		catch (Exception exception) {
			return Response.status(404).build();
		}		
	}
	
	
	private LocalDateTime convertLongToLocalDateTime(final Long startEpoch) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(startEpoch), TimeZone.getDefault().toZoneId());
}
	
}
