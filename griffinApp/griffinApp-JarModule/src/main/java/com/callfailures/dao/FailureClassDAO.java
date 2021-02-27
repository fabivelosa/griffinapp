package com.callfailures.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.callfailures.entity.FailureClass;


@Stateless
@LocalBean
public class FailureClassDAO {


	@PersistenceContext
	EntityManager em;


	public FailureClass getFailureClass(final int failClassId) {
		return em.find(FailureClass.class, failClassId);
	}


	public void create(final FailureClass obj) {
		em.persist(obj);
	}
}
