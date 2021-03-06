package com.callfailures.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.callfailures.entity.MarketOperator;

@Stateless
@LocalBean
public class MarketOperatorDAO {

	@PersistenceContext
	EntityManager entityManager;

	public MarketOperator getMarketOperator(final Object marketOperatorId) {
		return entityManager.find(MarketOperator.class, marketOperatorId);
	}

	public void create(final MarketOperator obj) {
		entityManager.persist(obj);
	}

}
