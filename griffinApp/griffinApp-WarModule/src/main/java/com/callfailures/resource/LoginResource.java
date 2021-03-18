package com.callfailures.resource;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.callfailures.entity.User;
import com.callfailures.services.UserService;

@Path("/login")
@Stateless
public class LoginResource {

	@Inject
	@EJB
	private UserService userService;

	@POST
	@Path("/auth")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED })
	public Response auth(final User user ) {
		final Boolean response = userService.validateUser(user); 
		if (response == Boolean.TRUE) {
			return Response.status(200).entity(user).build();
		}

		return Response.status(401).build();
	}

}
