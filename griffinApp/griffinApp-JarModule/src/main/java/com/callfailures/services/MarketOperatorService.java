package com.callfailures.services;

import javax.ejb.Local;
import java.io.File;
import java.util.List;
import java.util.Map;

import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.entity.Events;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;

@Local
public interface MarketOperatorService {

	MarketOperator findById(final MarketOperatorPK id);

	void create(MarketOperator obj);
	
	Map<String, List<MarketOperator>> read(File workbookFile);

}
