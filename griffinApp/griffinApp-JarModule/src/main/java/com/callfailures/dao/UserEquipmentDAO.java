package com.callfailures.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.callfailures.entity.UserEquipment;

@Stateless
@LocalBean
public class UserEquipmentDAO {
	private static final String FIND_ALL_USER_EQUIPMENT = "SELECT u FROM ue u";
	
	@PersistenceContext
	EntityManager entityManager;

	public UserEquipment getUserEquipment(final int ueId) {
		return entityManager.find(UserEquipment.class, ueId);
	}

	public void create(final UserEquipment obj) {
		entityManager.persist(obj);
	}

	public List<UserEquipment> findAll(){
		final Query query = entityManager.createQuery(FIND_ALL_USER_EQUIPMENT, UserEquipment.class);
		return query.getResultList();
	}
	
}
