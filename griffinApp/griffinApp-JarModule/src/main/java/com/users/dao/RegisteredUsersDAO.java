package com.users.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.users.entity.User;

@Stateless
@LocalBean
public class RegisteredUsersDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	public void addUser(User user) {
		em.persist(user);
	}
	
	public void updateUser(User user) {
		em.merge(user);
	}

}
