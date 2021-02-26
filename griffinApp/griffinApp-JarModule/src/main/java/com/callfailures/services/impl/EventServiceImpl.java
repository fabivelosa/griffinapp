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

import com.callfailures.dao.EventDAO;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.services.EventService;
import com.callfailures.services.ValidationService;

@Stateless
public class EventServiceImpl implements EventService {

	@Inject
	EventDAO eventDAO;

	@Inject
	ValidationService validationService;

	@Override
	public Events findById(final int id) {

		return eventDAO.getEvent(id);
	}

	@Override
	public void create(Events event) {
		eventDAO.create(event);

	}

	@Override
	public List<Events> read(File workbookFile) {
		List<Events> list = new ArrayList<>();

		try {

			Workbook workbook = new XSSFWorkbook(workbookFile);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();
			System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
			Iterator<Row> rowIterator = sheet.rowIterator();

			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();

				Cell cell = cellIterator.next();
				System.out.print(cell + "\t");
				Events event = new Events();

//				failureClass.setFailureClass(new Double((cell.getNumericCellValue())).intValue());

				cell = cellIterator.next();
//				failureClass.setFailureDesc(cell.getStringCellValue());
				list.add(event);
//				eventDAO.create(event);

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
