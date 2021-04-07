package com.callfailures.services;

import javax.ejb.Local;

import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface MarketOperatorService {

	MarketOperator findById(final MarketOperatorPK marketOperatorPK);

	void create(MarketOperator obj);

	ParsingResponse<MarketOperator> read(final Workbook workbook);

}
