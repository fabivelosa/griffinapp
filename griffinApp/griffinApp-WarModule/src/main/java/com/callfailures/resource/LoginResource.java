package com.callfailures.resource;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.callfailures.entity.TokenUtil;
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
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response auth(final User user) {
		final User authUser = userService.getUserByName(user.getUserName());

		if (authUser != null && User.passDecrypt(authUser.getUserPassword()).equals(user.getUserPassword())) {
			TokenUtil.issueNewToken(authUser);
			return Response.status(200).entity(authUser).build();
		}
		return Response.status(401).build();
	}

	@GET
	@Path("/logout/{token}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response logout(@PathParam("token") final String token) {
		TokenUtil.revokeToken(token);
		return Response.ok().build();
	}

}