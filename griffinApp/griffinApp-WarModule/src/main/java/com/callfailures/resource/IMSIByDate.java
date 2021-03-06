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

import com.callfailures.entity.Events;
import com.callfailures.entity.Secured;
import com.callfailures.entity.views.IMSICount;
import com.callfailures.entity.views.UniqueIMSI;
import com.callfailures.errors.ErrorMessage;
import com.callfailures.errors.ErrorMessages;
import com.callfailures.exception.InvalidDateException;
import com.callfailures.exception.InvalidIMSIException;
import com.callfailures.services.EventService;
import com.callfailures.utils.DateConverterUtil;

@Path("/IMSIs")
@Stateless
public class IMSIByDate {

	//As a Support Engineer I want to see a list of all IMSIs with call failures during a given time period
	//http://localhost:8080/callfailures/api/IMSIs/query?from=1578762900000&to=1578763800000
	@EJB
	private  EventService eventService;

	
	/**
	 * Queries List of IMSIs with failures during a given time period
	 * @param fromEpoch -the starting Date parameter converted to long or UNIX timestamp
	 * @param toEpoch the starting Date parameter converted to long or UNIX timestamp
	 * @param imsi - Optional IMSI
	 * @return List of Events if IMSI is present in the query param, ereturns list of unique IMSI names
	 */
	@GET
	@Secured
    @Path("/query")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIMSIsByDate(
			@QueryParam("from") final Long fromEpoch,
			@QueryParam("to") final Long toEpoch,
			@QueryParam("imsi") final String imsi) {
	
		final LocalDateTime startTime = DateConverterUtil.convertLongToLocalDateTime(fromEpoch); 
		final LocalDateTime endTime = DateConverterUtil.convertLongToLocalDateTime(toEpoch); 
		
		System.out.println("THE IMSI PARAMETER IS " + imsi);
		
		try {
			if(imsi == null || imsi.isEmpty()) {
				final List<UniqueIMSI> imsis = eventService.findIMSISBetweenDates(startTime, endTime);
				return Response.status(200).entity(imsis).build();
			}else {
				final List<Events> events = eventService.findListofIMSIEventsByDate(imsi, startTime, endTime);
				return Response.status(200).entity(events).build();
			}
		}catch (InvalidDateException exception){
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_DATE.getMessage())).build();
		}catch (InvalidIMSIException exception) {
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_IMSI.getMessage())).build();
		}finally {
			System.out.println("query called 70");
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
		
		final LocalDateTime startTime = DateConverterUtil.convertLongToLocalDateTime(fromEpoch); 
		final LocalDateTime endTime = DateConverterUtil.convertLongToLocalDateTime(toEpoch); 
		
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
	
	//http://localhost:8080/callfailures/api//IMSIs/query/failureClass?=0
	@GET
	@Secured
	@Path("/query/failureClass")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIMSIsByFailure(@QueryParam("failureClass")final int failureClass) {
		
		try {
			final List<UniqueIMSI> imsis = eventService.findIMSISByFailure(failureClass);
			return Response.status(200).entity(imsis).build();
		} catch (Exception e) {
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_FAILURECLASS.getMessage())).build();
		}
		
	}
	
}
