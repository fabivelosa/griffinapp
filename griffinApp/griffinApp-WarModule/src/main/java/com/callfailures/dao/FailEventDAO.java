package com.callfailures.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.callfailures.entity.CallFailure;

@Stateless
@LocalBean
public class FailEventDAO {

	@PersistenceContext
	private EntityManager em;
	
	
	public void save(CallFailure callFail) {
		em.persist(callFail);
	}
	
	public void update(CallFailure callFail) {
		em.merge(callFail);
	}
	
}
	

