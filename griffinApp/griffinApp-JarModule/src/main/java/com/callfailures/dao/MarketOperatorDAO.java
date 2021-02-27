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
	EntityManager em;

	public MarketOperator getMarketOperator(final Object marketOperatorId) {
		return em.find(MarketOperator.class, marketOperatorId);
	}

	public void create(final MarketOperator obj) {
		em.persist(obj);
	}

}
