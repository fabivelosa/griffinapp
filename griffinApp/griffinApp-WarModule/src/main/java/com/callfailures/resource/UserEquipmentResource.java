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

import com.callfailures.entity.Secured;
import com.callfailures.entity.UserEquipment;
import com.callfailures.entity.views.PhoneFailures;
import com.callfailures.services.EventService;
import com.callfailures.services.UserEquipmentService;

@Path("/userEquipment")
@Stateless
public class UserEquipmentResource {

	@EJB
	private UserEquipmentService userEquipmentService;
	
	@EJB
	private EventService eventService;
	
	@GET
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response findAll() {
		final List<UserEquipment> allPhoneModels = userEquipmentService.findAll();
		return Response.status(200).entity(allPhoneModels).build();
	}
	
	
	@GET
	@Secured
    @Path("/query")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findUniqueEventCauseCountByPhoneModel(
			@QueryParam("tac") final int tac
			) {
		final List<PhoneFailures> phoneFailures =  eventService.findUniqueEventCauseCountByPhoneModel(tac);
		return Response.status(200).entity(phoneFailures).build();
	}
}
