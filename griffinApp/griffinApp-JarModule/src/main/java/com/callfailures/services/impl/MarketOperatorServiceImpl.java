package com.callfailures.services.impl;

import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.dao.MarketOperatorDAO;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.MarketOperatorService;
import com.callfailures.services.ValidationService;

@Stateless
public class MarketOperatorServiceImpl implements MarketOperatorService {

	@Inject
	MarketOperatorDAO marketOperatorDAO;

	@Inject
	ValidationService validationService;

	@Override
	public MarketOperator findById(final MarketOperatorPK marketOperatorPK) {
		return marketOperatorDAO.getMarketOperator(marketOperatorPK);
	}

	@Override
	public void create(final MarketOperator obj) {
		marketOperatorDAO.create(obj);
	}

	@Override
	public ParsingResponse<MarketOperator> read(final Workbook workbook) {
		final ParsingResponse<MarketOperator> result = new ParsingResponse<>();

		final Sheet sheet = workbook.getSheetAt(4);
		final Iterator<Row> rowIterator = sheet.rowIterator();
		MarketOperator operator = null;
		MarketOperatorPK operatorPK = null;

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			final Iterator<Cell> cellIterator = row.cellIterator();

			Cell cell = cellIterator.next();
			try {
				operator = new MarketOperator();
				operatorPK = new MarketOperatorPK();
				operatorPK.setCountryCode((int) cell.getNumericCellValue());
				cell = cellIterator.next();
				operatorPK.setOperatorCode((int) cell.getNumericCellValue());
				operator.setMarketOperatorId(operatorPK);
				cell = cellIterator.next();
				operator.setCountryDesc(cell.getStringCellValue());
				cell = cellIterator.next();
				operator.setOperatorDesc(cell.getStringCellValue());
				if (validationService.checkExistingMarketOperator(operator) == null) {
					marketOperatorDAO.create(operator);
					result.addValidObject(operator);
				}
			} catch (Exception e) {
				result.addInvalidRow(new InvalidRow(cell.getRowIndex(), e.getMessage()));
			}
		}

		return result;
	}
}
