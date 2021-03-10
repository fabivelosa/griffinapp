package com.callfailures.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.callfailures.entity.User;

@Stateless
@LocalBean
public class UsersDAO {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<User> getRegisteredUsers() {
		Query query = em.createQuery("SELECT u FROM User u");
		return query.getResultList();
	}

	public User getUserById(String id) {
		return em.find(User.class, id);
	}
	
	public User getUserByName(String name) {
		
			
		 User user = null;
		    Query query = em.createQuery("SELECT u FROM users u WHERE u.userName=:userName");
		    query.setParameter("userName", name);
		    try {
		        user = (User) query.getSingleResult();
		        System.out.println(user.getUserPassword());
		    } catch (Exception e) {
		        // Handle exception
		    }
		    return user;
	}

	public void addUser(User user) {
		em.persist(user);
	}

	public void updateUser(User user) {
		em.merge(user);
	}

}
