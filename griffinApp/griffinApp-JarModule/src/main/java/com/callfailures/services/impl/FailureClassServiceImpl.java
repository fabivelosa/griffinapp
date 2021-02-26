package com.callfailures.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.callfailures.services.ValidationService;

@Stateless
public class FailureClassServiceImpl implements FailureClassService {

	@Inject
	FailureClassDAO failureClassDAO;

	@Inject
	ValidationService validationService;


	@Override
	public FailureClass findById(final int id) {

		return failureClassDAO.getFailureClass(id);
	}

	@Override
	public void create(FailureClass obj) {
		failureClassDAO.create(obj);

	}

	@Override
	public List<FailureClass> read(File workbookFile) {
		List<FailureClass> list = new ArrayList<FailureClass>();

		try {

			Workbook workbook = new XSSFWorkbook(workbookFile);
			Sheet sheet = workbook.getSheetAt(2);
			Iterator<Row> iterator = sheet.iterator();
			System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
			Iterator<Row> rowIterator = sheet.rowIterator();

			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();

				Cell cell = cellIterator.next();
				System.out.print(cell + "\t");
				FailureClass failureClass = new FailureClass();

				failureClass.setFailureClass(new Double((cell.getNumericCellValue())).intValue());

				cell = cellIterator.next();
				failureClass.setFailureDesc(cell.getStringCellValue());
				list.add(failureClass);
				failureClassDAO.create(failureClass);

			}
			System.out.println();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
