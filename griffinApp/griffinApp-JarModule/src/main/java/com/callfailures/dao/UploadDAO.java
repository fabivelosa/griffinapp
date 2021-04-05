package com.callfailures.dao;

import java.util.UUID;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.callfailures.entity.Upload;

@Stateless
@LocalBean
public class UploadDAO {

	@PersistenceContext
	EntityManager entityManager;

	public Upload getUploadByRef(final UUID uploadRef) {
		return entityManager.find(Upload.class, uploadRef);
	}

	public void create(final Upload obj) {
		entityManager.persist(obj);
		entityManager.flush();
	}

	public void update(final Upload obj) {
		entityManager.merge(obj);
		entityManager.flush();
	}

}
