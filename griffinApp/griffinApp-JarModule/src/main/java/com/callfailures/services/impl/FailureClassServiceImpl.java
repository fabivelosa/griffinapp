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

import com.callfailures.dao.FailureClassDAO;
import com.callfailures.entity.FailureClass;
import com.callfailures.services.FailureClassService;



 
@Stateless
public class FailureClassServiceImpl implements FailureClassService {

	@Inject
	FailureClassDAO failureClassDAO;

	@Override
	public FailureClass findById(final int id) {

		return failureClassDAO.getFailureClass(id);
	}

	@Override
	public void create(final FailureClass obj) {
		failureClassDAO.create(obj);

	}

	@Override
	public Map<String, List<FailureClass>> read(final File workbookFile) {

		final Map<String, List<FailureClass>> resut = new HashMap<String, List<FailureClass>>();
		final List<FailureClass> listSucess = new ArrayList<FailureClass>();
		final List<FailureClass> listError = new ArrayList<FailureClass>();

		try {

			final Workbook workbook = new XSSFWorkbook(workbookFile);
			final Sheet sheet = workbook.getSheetAt(2);
			final Iterator<Row> iterator = sheet.iterator();
			System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
			final Iterator<Row> rowIterator = sheet.rowIterator();
			FailureClass failureClass = null;
			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next(); 

				final Iterator<Cell> cellIterator = row.cellIterator();

				Cell cell = cellIterator.next();
				System.out.print(cell + "\t");
				failureClass = new FailureClass();

				failureClass.setFailureClass(new Double(cell.getNumericCellValue()).intValue());

				cell = cellIterator.next();
				failureClass.setFailureDesc(cell.getStringCellValue());

				try {
					failureClassDAO.create(failureClass);
					
					listSucess.add(failureClass);
				} catch (Exception e) {
					//failureClass.setStatusMessage(e.getMessage());
					listError.add(failureClass);
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

		resut.put("SUCCESS", (ArrayList<FailureClass>) listSucess);
		resut.put("ERROR", (ArrayList<FailureClass>) listError);

		return resut;
	}

}
