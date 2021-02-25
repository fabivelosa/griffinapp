package com.cailfailures.login;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/login") 
@Stateless
public class Login {

	
	
	@GET
	@Produces({MediaType.TEXT_PLAIN})
	@Path("/{query}")
	public Response checkLogin(@PathParam("query") String username) {
		
		if (isAuthenticated(username)) {
			//return Response.status(200).entity(user).build();
		}
		
		/*User user = userDAO.findUserByUsername(username);
		if (person != null) {
		
			validate password();
			return Response.status(200).entity(user).build();
		}
		
		*/
		
		return Response.status(400).build();
	}
	
	
	private boolean isAuthenticated(String auth) {
		String authorize = "";
		return false;
	}
	/*
	private void validateUser(User user) {
		
	}
	
	*/
	
	/*
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/query")
	public Response findUserByUsername(
					@QueryParam("username") String username,
					@QueryParam("password")String password) {
		
		
		return Response.status(200).entity().build();
	} */
	
	
}
