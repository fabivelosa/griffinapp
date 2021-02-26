package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;

@Local
public interface MarketOperatorService {

	MarketOperator findById(final MarketOperatorPK id);

	void create(MarketOperator obj);
}
