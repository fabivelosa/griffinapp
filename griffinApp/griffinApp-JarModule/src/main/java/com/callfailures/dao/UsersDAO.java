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
public class UsersDAO {

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<User> getRegisteredUsers() {
		final Query query = entityManager.createQuery("SELECT u FROM users u");
		return query.getResultList();
	}

	public User getUserByUserId(final String userId) {
		return entityManager.find(User.class, userId);
	}
	

	public User getUserByName(final String name) {
		User user;
		final Query query = entityManager.createQuery("SELECT u FROM users u WHERE u.userName=:userName");
		query.setParameter("userName", name);
		try {
			user = (User) query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}
		return user;
	}

	public void addUser(final User user) {
		entityManager.persist(user);
	}

	public void updateUser(final User user) {
		entityManager.merge(user);
	}

}
