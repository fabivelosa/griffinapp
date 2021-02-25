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
	private EntityManager em;

	public FailureClass getFailureClass(int failClassId) {
		return em.find(FailureClass.class, failClassId);
	}

	public void create(FailureClass obj) {
		em.persist(obj);
	}
}
