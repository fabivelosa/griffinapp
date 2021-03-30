package com.callfailures.resource;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.callfailures.entity.Secured;
import com.callfailures.entity.User;
import com.callfailures.services.UserService;

@Path("/users")
@Stateless
@LocalBean
public class UserResource {

	@EJB
	private UserService userService;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{id}")
	@Secured
	public Response findUserById(final @PathParam("id") String userId) {
		final User user = userService.getUserById(userId);
		return Response.status(200).entity(user).build();
	}

	@POST
	@Consumes("application/json")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addUser(final User user) {
		user.setUserPassword(User.passEncrypt(user.getUserPassword()));
		userService.addUser(user);
		return Response.status(200).entity(user).build();
	}

	@PUT
	@Consumes("application/json")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateUser(final User user) {
		userService.updateUser(user);
		return Response.status(200).entity(user).build();

	}

}
