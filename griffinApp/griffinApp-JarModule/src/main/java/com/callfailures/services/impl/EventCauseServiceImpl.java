package com.callfailures.services.impl;

import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.dao.EventCauseDao;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.EventCauseService;
import com.callfailures.services.ValidationService;

@Stateless
public class EventCauseServiceImpl implements EventCauseService {

	@Inject
	EventCauseDao eventCauseDAO;

	@Inject
	ValidationService validationService;

	@Override
	public EventCause findById(final EventCausePK eventCausePK) {
		return eventCauseDAO.getEventCause(eventCausePK);
	}

	@Override
	public void create(final EventCause obj) {
		eventCauseDAO.create(obj);
	}

	@Override
	public ParsingResponse<EventCause> read(final Workbook workbook) {
		final ParsingResponse<EventCause> result = new ParsingResponse<>();

		final Sheet sheet = workbook.getSheetAt(1);
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
			eventCausePK.setCauseCode((int) cell.getNumericCellValue());
			cell = cellIterator.next();
			eventCausePK.setEventCauseId((int) cell.getNumericCellValue());
			cell = cellIterator.next();
			eventCause.setEventCauseId(eventCausePK);
			eventCause.setDescription(cell.getStringCellValue());

			try {
				if (validationService.checkExistingEventCause(eventCause) == null) {
					eventCauseDAO.create(eventCause);
					result.addValidObject(eventCause);
				}
			} catch (Exception e) {
				result.addInvalidRow(new InvalidRow(cell.getRowIndex(), e.getMessage()));
			}
		}

		return result;
	}
}
