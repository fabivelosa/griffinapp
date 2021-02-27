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
import com.callfailures.exception.FieldNotValidException;
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
		List<Integer> validRows = new ArrayList<>();
		List<Events> list = new ArrayList<>();

		try(Workbook workbook = new XSSFWorkbook(workbookFile);) {

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.rowIterator();

			Row row = rowIterator.next();

			int rowNumber = 0;

			while (rowIterator.hasNext()) {
				rowNumber++;
				row = rowIterator.next();

				try {
					Events events = createEventObject(row);

					eventDAO.create(events);

					list.add(events);
				}catch(FieldNotValidException e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
		}

		return list;
	}

	
	private Events createEventObject(Row row) {
		Events events = new Events();

		events.setDateTime(validationService.checkDate(row, 0));

		events.setCellId(validationService.checkCellId(row, 6));

		events.setDuration(validationService.checkDuration(row, 7));

		events.setNeVersion(validationService.checkNEVersion(row, 9));

		events.setImsi(validationService.checkIMSI(row, 10));

		events.setHier3Id(validationService.checkhier3Id(row, 11));

		events.setHier32Id(validationService.checkhier32Id(row, 12));

		events.setHier321Id(validationService.checkhier321Id(row, 13));

		events.setEventCause(validationService.checkExistingEventCause(row, 1, 2));

		events.setFailureClass(validationService.checkExistingFailureClass(row, 2));

		events.setUeType(validationService.checkExistingUserEquipmentType(row, 3));

		events.setMarketOperator(validationService.checkExistingMarketOperator(row, 4,5));

		System.out.println(events);

		validationService.validate(events);
		return events;
	}

}
