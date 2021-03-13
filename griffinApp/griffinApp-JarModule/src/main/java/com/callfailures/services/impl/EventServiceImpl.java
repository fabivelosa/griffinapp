package com.callfailures.services.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.callfailures.dao.EventDAO;
import com.callfailures.entity.Events;
import com.callfailures.entity.views.IMSISummary;
import com.callfailures.entity.views.PhoneModelSummary;
import com.callfailures.exception.FieldNotValidException;
import com.callfailures.exception.InvalidDateException;
import com.callfailures.exception.InvalidIMSIException;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.EventService;
import com.callfailures.services.ValidationService;

@Stateless
public class EventServiceImpl implements EventService {

	@Inject
	EventDAO eventDAO;

	@Inject
	ValidationService validationService;

	
	@Override
	public IMSISummary findCallFailuresCountByIMSIAndDate(final String imsi, final LocalDateTime startTime, final LocalDateTime endTime) {
		if(startTime.isAfter(endTime)) {
			throw new InvalidDateException();
		}
	
		if(!isValidIMSI(imsi)) {
			throw new InvalidIMSIException();
		}
		
		return eventDAO.findCallFailuresCountByIMSIAndDate(imsi, startTime, endTime);
	}
	
	@Override
	public	PhoneModelSummary findCallFailuresCountByPhoneModelAndDate(final String model, final LocalDateTime startTime, final LocalDateTime endTime) {
		if(startTime == null || endTime == null ) {
			return null;
		}
	
		return eventDAO.findCallFailuresCountByPhoneModelAndDate(model, startTime, endTime);
	}
	
	
	@Override
	public ParsingResponse<Events> read(final File workbookFile) {
		final ParsingResponse<Events> parsingResult = new ParsingResponse<>();
		try (Workbook workbook = new XSSFWorkbook(workbookFile);) {
			final Sheet sheet = workbook.getSheetAt(0);
			final Iterator<Row> rowIterator = sheet.rowIterator();
			Row row = rowIterator.next();
			int rowNumber = 0;
			while (rowIterator.hasNext()) {
				rowNumber++;
				row = rowIterator.next();
				try {
					final Events events = createEventObject(row);
					eventDAO.create(events);
					parsingResult.addValidObject(events);
				} catch (FieldNotValidException e) {
					parsingResult.addInvalidRow(new InvalidRow(rowNumber, e.getMessage()));
				}
			}
		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
		}
		return parsingResult;
	}

	
	private boolean isValidIMSI(final String imsi) {
		if(imsi == null || imsi.length() > 15) {
			return false;
		}

		for(int i = 0; i < imsi.length(); i++) {
			if(!Character.isDigit(imsi.charAt(0))) {
				return false;
			}
		}
		return true;
	}
	

	private boolean isValidPhoneModel(final String ueType) {
		if(ueType == null || ueType.length() > 15) {
			return false;
		}

		for(int i = 0; i < ueType.length(); i++) {
			if(!Character.isDigit(ueType.charAt(0))) {
				return false;
			}
		}
		return true;
	}
	
	
	private Events createEventObject(final Row row) {
		final Events events = new Events();
		validateNonDatabaseDependentFields(row, events);
		validateDatabaseDependendentFields(row, events);
		validationService.validate(events);
		return events;
	}

	private void validateDatabaseDependendentFields(final Row row, final Events events) {
		events.setHier321Id(validationService.checkhier321Id(row, 13));
		events.setEventCause(validationService.checkExistingEventCause(row, 1, 8));
		events.setFailureClass(validationService.checkExistingFailureClass(row, 2));
		events.setUeType(validationService.checkExistingUserEquipmentType(row, 3));
		events.setMarketOperator(validationService.checkExistingMarketOperator(row, 4, 5));
	}

	private void validateNonDatabaseDependentFields(final Row row, final Events events) {
		events.setDateTime(validationService.checkDate(row, 0));
		events.setCellId(validationService.checkCellId(row, 6));
		events.setDuration(validationService.checkDuration(row, 7));
		events.setNeVersion(validationService.checkNEVersion(row, 9));
		events.setImsi(validationService.checkIMSI(row, 10));
		events.setHier3Id(validationService.checkhier3Id(row, 11));
		events.setHier32Id(validationService.checkhier32Id(row, 12));
	}

}
