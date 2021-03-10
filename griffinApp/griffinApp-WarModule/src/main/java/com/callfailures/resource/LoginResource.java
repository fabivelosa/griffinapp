package com.callfailures.resource;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.callfailures.services.UserService;

@Path("/login")
@Stateless
@LocalBean
public class LoginResource {

	@Inject
	@EJB
	private UserService userService;
	
	@POST
	@Path("/auth")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response auth(@FormParam("username") String username , @FormParam("password") String password   ) {
		Boolean user = userService.validateUser(username, password);
		if(user == Boolean.TRUE) {
			return Response.status(200).entity(user).build();
		}
		
		return Response.status(401).build();
	}

	 

}
