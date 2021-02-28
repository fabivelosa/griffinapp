package com.callfailures.services.impl;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.callfailures.services.MarketOperatorService;
import com.callfailures.services.ValidationService;

@Stateless
public class MarketOperatorServiceImpl implements MarketOperatorService {

	@Inject
	MarketOperatorDAO marketOperatorDAO;

	@Inject
	ValidationService validationService;

	@Override
	public MarketOperator findById(final MarketOperatorPK id) {
		return marketOperatorDAO.getMarketOperator(id);
	}

	@Override
	public void create(final MarketOperator obj) {
		marketOperatorDAO.create(obj);
	}


	@Override
	public Map<String, List<MarketOperator>> read(final File workbookFile){

		final Map<String, List<MarketOperator>> result = new HashMap<String, List<MarketOperator>>();
		final List<MarketOperator> listSuccess = new ArrayList<MarketOperator>();
		final List <MarketOperator> listError = new ArrayList<MarketOperator>();

		try {
			final Workbook workbook = new XSSFWorkbook(workbookFile);
			final Sheet sheet = workbook.getSheetAt(1);
			final Iterator<Row> iterator = sheet.iterator();
			System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
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
				operatorPK.setCountryCode(new Double(cell.getNumericCellValue()).intValue());
				cell = cellIterator.next();
				operatorPK.setOperatorCode(new Double(cell.getNumericCellValue()).intValue());
				cell = cellIterator.next();


				operator.setMarketOperatorId(operatorPK);
				operator.setCountryDesc(cell.getStringCellValue());
				operator.setOperatorDesc(cell.getStringCellValue());

				try {
					if (validationService.checkExistingMarketOperator(operator) == null) {
						marketOperatorDAO.create(operator);
						listSuccess.add(operator);
					}
				} catch (Exception e) {
					listError.add(operator);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

		result.put("SUCCESS", (ArrayList<MarketOperator>) listSuccess);
		result.put("ERROR", (ArrayList<MarketOperator>) listError);

		return result;
	}


}

