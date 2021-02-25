package com.callfailures.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.UserEquipment;

@Stateless
@LocalBean
public class UserEquipmentDAO {

	@PersistenceContext
	private EntityManager em;

	public UserEquipment getUserEquipment(int ueId) {
		return em.find(UserEquipment.class, ueId);
	}

	public void create(UserEquipment obj) {
		em.persist(obj);
	}

}
