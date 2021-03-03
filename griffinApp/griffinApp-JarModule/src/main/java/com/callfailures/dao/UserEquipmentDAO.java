package com.callfailures.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.callfailures.entity.UserEquipment;

@Stateless
@LocalBean
public class UserEquipmentDAO {

	@PersistenceContext
	EntityManager entityManager;

	public UserEquipment getUserEquipment(final int ueId) {
		return entityManager.find(UserEquipment.class, ueId);
	}

	public void create(final UserEquipment obj) {
		entityManager.persist(obj);
	}

}
