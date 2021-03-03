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
	EntityManager entityManager;

	public Events getEvent(final int eventId) {
		return entityManager.find(Events.class, eventId);
	}

	public void create(final Events obj) {
		entityManager.persist(obj);
	}

}
