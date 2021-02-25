package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;

@Local
public interface MarketOperatorService {

	public MarketOperator findById(final MarketOperatorPK id);

	public void create(MarketOperator obj);
}
