package com.callfailures.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
	public ParsingResponse<MarketOperator> read(final File workbookFile) {
		final ParsingResponse<MarketOperator> result = new ParsingResponse<>();

		try (Workbook workbook = new XSSFWorkbook(workbookFile)) {
			final Sheet sheet = workbook.getSheetAt(4);
			final Iterator<Row> rowIterator = sheet.rowIterator();
			MarketOperator operator = null;
			MarketOperatorPK operatorPK = null;

			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				final Iterator<Cell> cellIterator = row.cellIterator();

				Cell cell = cellIterator.next();
				operator = new MarketOperator();
				operatorPK = new MarketOperatorPK();
				operatorPK.setCountryCode((int) cell.getNumericCellValue());
				cell = cellIterator.next();
				operatorPK.setOperatorCode((int) cell.getNumericCellValue());
				cell = cellIterator.next();

				operator.setMarketOperatorId(operatorPK);
				operator.setCountryDesc(cell.getStringCellValue());
				cell = cellIterator.next();
				operator.setOperatorDesc(cell.getStringCellValue());

				try {
					if (validationService.checkExistingMarketOperator(operator) == null) {
						marketOperatorDAO.create(operator);
						result.addValidObject(operator);
					}
				} catch (Exception e) {
					result.addInvalidRow(new InvalidRow(cell.getRowIndex(), e.getMessage()));
				}
			}
		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
		}
		return result;
	}
}
