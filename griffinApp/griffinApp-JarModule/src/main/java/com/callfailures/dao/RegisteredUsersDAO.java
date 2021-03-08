package com.callfailures.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.callfailures.entity.User;

@Stateless
@LocalBean
public class RegisteredUsersDAO {

	@PersistenceContext
	private EntityManager em;

	public List<User> getRegisteredUsers() {
		Query query = em.createQuery("SELECT u FROM User u");
		return query.getResultList();
	}

	public User getUserById(String id) {
		return em.find(User.class, id);
	}

	public void addUser(User user) {
		em.persist(user);
	}

	public void updateUser(User user) {
		em.merge(user);
	}

}
