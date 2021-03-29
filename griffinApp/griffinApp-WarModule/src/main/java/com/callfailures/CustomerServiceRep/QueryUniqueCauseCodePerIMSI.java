package com.callfailures.CustomerServiceRep;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.callfailures.entity.Secured;
import com.callfailures.errors.ErrorMessage;
import com.callfailures.errors.ErrorMessages;
import com.callfailures.exception.InvalidIMSIException;
import com.callfailures.services.EventService;

@Path("/causecodes")
@Stateless
public class QueryUniqueCauseCodePerIMSI {

	@EJB
	private EventService eventService;
	
	/**
	 * Customer Representative: Display unique cause code per imsi
	 * @param imsi - the IMSI parameter
	 * @return Return unique list of cause codes
	 */
	@GET @Path("{imsi}")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response findCauseCodeByIMSI(@PathParam("imsi") final String imsi) {
		try {
			return Response.status(200).entity(eventService.findUniqueCauseCode(imsi)).build();
		} catch (InvalidIMSIException exception) {
			return Response.status(404).entity(new ErrorMessages(ErrorMessage.INVALID_IMSI.getMessage())).build();
		}
	}
	
}
