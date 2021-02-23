package com.callfailures.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.callfailures.entity.CallFailure;
import com.callfailures.dao.*;

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
	

