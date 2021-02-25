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
	private EntityManager em;

	public MarketOperator getMarketOperator(Object marketOperatorId) {
		return em.find(MarketOperator.class, marketOperatorId);
	}

}
