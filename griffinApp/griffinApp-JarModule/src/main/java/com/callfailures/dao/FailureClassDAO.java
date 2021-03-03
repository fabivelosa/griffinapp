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
	EntityManager entityManager;


	public FailureClass getFailureClass(final int failClassId) {
		return entityManager.find(FailureClass.class, failClassId);
	}


	public void create(final FailureClass obj) {
		entityManager.persist(obj);
	}
}
