package com.callfailures.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.callfailures.entity.EventCause;

@Stateless
@LocalBean
public class EventCauseDAO {

	@PersistenceContext
	EntityManager em;

	public EventCause getEventCause(final Object eventCauseId) {
		return em.find(EventCause.class, eventCauseId);
	}

	public void create(final EventCause obj) {
		em.persist(obj);
	}
}
