package com.callfailures.resource;

import java.util.List;

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

import com.callfailures.dao.RegisteredUsersDAO;
import com.callfailures.entity.User;

@Path("/users")
@Stateless
@LocalBean
public class UserResource {

	@EJB
	private RegisteredUsersDAO userDao;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findAll() {
		System.out.println("Show registered users");
		List<User> users = userDao.getRegisteredUsers();
		return Response.status(200).entity(users).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{id}")
	public Response findUserById(@PathParam("id") String id) {
		User user = userDao.getUserById(id);
		return Response.status(200).entity(user).build();
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addUser(User user) {
		userDao.addUser(user);
		return Response.status(200).entity(user).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes("application/json")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateUser(User user) {
		userDao.updateUser(user);
		return Response.status(200).entity(user).build();

	}

}
