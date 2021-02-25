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
	private EntityManager em;

	public EventCause getEventCause(Object eventCauseId) {
		return em.find(EventCause.class, eventCauseId); 
	}
	
}
