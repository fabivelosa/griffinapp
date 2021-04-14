package com.callfailures.resource;

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
import com.callfailures.services.EventService;

@Path("/cells")
@Stateless
public class CellResource {

	@EJB
	private  EventService eventService;

	
	/**
	 * Queries List of Events of Market Operator Cell ID
	 * @param cellId
	 * @param country
	 * @param operator
	 * @return List of Events of Market Operator Cell ID
	 */
	@GET
	@Secured
    @Path("/query")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIMSIsByDate(
			@QueryParam("cellId") final int cellId,
			@QueryParam("country") final String country,
			@QueryParam("operator") final String operator) {
	
		final List<Events> events = eventService.findListofIMSIEventsByMarketOperatorCellID(cellId, country, operator);
		return Response.status(200).entity(events).build();
	}
	
}
