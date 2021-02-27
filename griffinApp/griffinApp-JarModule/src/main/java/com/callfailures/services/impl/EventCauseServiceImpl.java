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

import com.callfailures.dao.EventCauseDAO;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.services.EventCauseService;
import com.callfailures.services.ValidationService;

@Stateless
public class EventCauseServiceImpl implements EventCauseService {

	@Inject
	EventCauseDAO eventCauseDAO;

	@Inject
	ValidationService validationService;

	@Override
	public EventCause findById(final EventCausePK id) {
		return eventCauseDAO.getEventCause(id);
	}

	@Override
	public void create(final EventCause obj) {
		eventCauseDAO.create(obj);
	}

	@Override
	public Map<String, List<EventCause>> read(final File workbookFile) {

		final Map<String, List<EventCause>> result = new HashMap<String, List<EventCause>>();
		final List<EventCause> listSucess = new ArrayList<EventCause>();
		final List<EventCause> listError = new ArrayList<EventCause>();

		try {
			final Workbook workbook = new XSSFWorkbook(workbookFile);
			final Sheet sheet = workbook.getSheetAt(1);
			final Iterator<Row> iterator = sheet.iterator();
			System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
			final Iterator<Row> rowIterator = sheet.rowIterator();
			EventCause eventCause = null;
			EventCausePK eventCausePK = null;

			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				final Iterator<Cell> cellIterator = row.cellIterator();

				Cell cell = cellIterator.next();
				eventCause = new EventCause();
				eventCausePK = new EventCausePK();
				eventCausePK.setCauseCode(new Double(cell.getNumericCellValue()).intValue());
				cell = cellIterator.next();
				eventCausePK.setEventCauseId(new Double(cell.getNumericCellValue()).intValue());
				cell = cellIterator.next();
				eventCause.setEventCauseId(eventCausePK);
				eventCause.setDescription(cell.getStringCellValue());

				try {
					if (validationService.checkExistingEventCause(eventCause) == null) {
						eventCauseDAO.create(eventCause);
						listSucess.add(eventCause);
					}
				} catch (Exception e) {
					listError.add(eventCause);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

		result.put("SUCCESS", (ArrayList<EventCause>) listSucess);
		result.put("ERROR", (ArrayList<EventCause>) listError);

		return result;
	}

}
