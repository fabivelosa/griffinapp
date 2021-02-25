package com.callfailures.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.callfailures.entity.Events;

@Stateless
@LocalBean
public class EventDAO {

	@PersistenceContext
	private EntityManager em;

	public Events getEvent(int eventId) {
		return em.find(Events.class, eventId);
	}

	public void create(Events obj) {
		em.persist(obj);
	}

}
