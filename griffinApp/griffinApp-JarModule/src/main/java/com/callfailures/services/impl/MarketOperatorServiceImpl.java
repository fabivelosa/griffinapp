package com.callfailures.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.callfailures.dao.MarketOperatorDAO;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.services.MarketOperatorService;
import com.callfailures.services.ValidationService;

@Stateless
public class MarketOperatorServiceImpl implements MarketOperatorService {

	@Inject
	MarketOperatorDAO marketOperatorDAO;

	@Inject
	ValidationService validationService;

	@Override
	public MarketOperator findById(MarketOperatorPK id) {
		return marketOperatorDAO.getMarketOperator(id);
	}

	@Override
	public void create(MarketOperator obj) {
		marketOperatorDAO.create(obj);
	}

}
