package com.cailfailures.login;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.users.dao.RegisteredUsersDAO;
import com.users.entity.User;


@Path("/login") 
@Stateless
public class Login {

	@EJB
	private RegisteredUsersDAO userDao;
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/checkLogin")
	public Response checkLogin(@HeaderParam("id") String auth) {
		
		if (isAuthenticated(auth)) {
			
		}
		
		/*
		System.out.println("id called");
		User user = userDao.getUserById(username);
		System.out.println(user);
		if (user != null) {
		//Check password
			return Response.status(200).entity(user).build();
		}
		*/
		
		
		return Response.status(400).build();
		
		
	}
	
	
	private boolean isAuthenticated(String auth) {
		
		//Decode String retreived from database
		String authorize = "";
		return false;
	}
	
	
}
