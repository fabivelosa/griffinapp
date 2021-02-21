package com.callfailures.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.callfailures.entity.CallFailure;

@Stateless
public class EventDAO {

	@PersistenceContext
	private EntityManager em;

	public CallFailure getEvent(int eventId) {
		return em.find(CallFailure.class, eventId);
	}

}
