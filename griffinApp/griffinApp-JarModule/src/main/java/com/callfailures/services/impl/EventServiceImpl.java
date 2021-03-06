package com.callfailures.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.callfailures.dao.EventDAO;
import com.callfailures.dao.UploadDAO;
import com.callfailures.entity.Events;
import com.callfailures.entity.Upload;
import com.callfailures.entity.views.DeviceCombination;
import com.callfailures.entity.views.IMSICount;
import com.callfailures.entity.views.IMSIEvent;
import com.callfailures.entity.views.IMSISummary;
import com.callfailures.entity.views.PhoneFailures;
import com.callfailures.entity.views.PhoneModelSummary;
import com.callfailures.entity.views.UniqueIMSI;
import com.callfailures.exception.FieldNotValidException;
import com.callfailures.exception.InvalidDateException;
import com.callfailures.exception.InvalidFailureClassException;
import com.callfailures.exception.InvalidIMSIException;
import com.callfailures.exception.InvalidPhoneModelException;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.EventService;
import com.callfailures.services.ValidationService;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class EventServiceImpl implements EventService {

	@Inject
	EventDAO eventDAO;

	@Inject
	UploadDAO uploadDAO;

	@Inject
	ValidationService validationService;

	@Override
	public List<IMSIEvent> findFailuresByImsi(final String imsi) {
		if (!isValidIMSI(imsi)) {
			return null;
		}
		return eventDAO.findEventsByIMSI(imsi);
	}

	@Override
	public List<Integer> findUniqueCauseCode(final String imsi) {
		if (!isValidIMSI(imsi)) {
			throw new InvalidIMSIException();
		}

		return eventDAO.findEventsByIMSI(imsi).stream()
				.map(imsiEvent -> imsiEvent.getEventCause().getEventCauseId().getCauseCode()).distinct()
				.collect(Collectors.toList());
	}

	@Override
	public IMSISummary findCallFailuresCountByIMSIAndDate(final String imsi, final LocalDateTime startTime,
			final LocalDateTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new InvalidDateException();
		}

		if (!isValidIMSI(imsi)) {
			throw new InvalidIMSIException();
		}

		return eventDAO.findCallFailuresCountByIMSIAndDate(imsi, startTime, endTime);
	}

	@Override
	public PhoneModelSummary findCallFailuresCountByPhoneModelAndDate(final String model, final LocalDateTime startTime,
			final LocalDateTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new InvalidDateException();
		}

		if (model.isEmpty()) {
			throw new InvalidPhoneModelException();
		}

		return eventDAO.findCallFailuresCountByPhoneModelAndDate(model, startTime, endTime);
	}

	@Override
	public List<PhoneFailures> findUniqueEventCauseCountByPhoneModel(final int tac) {
		return eventDAO.findUniqueEventCauseCountByPhoneModel(tac);
	}

	@Override
	public ParsingResponse<Events> read(final Sheet sheet, final int ini, final int end, final Upload currentUpload) {

		//final List<Events> eventsToProcess = new ArrayList<Events>();
		final ParsingResponse<Events> parsingResult = new ParsingResponse<>();
		int rowTotal = sheet.getLastRowNum();
		if ((rowTotal > 0) || sheet.getPhysicalNumberOfRows() > 0) {
			rowTotal++;
		}
		readRows(currentUpload, parsingResult, sheet, ini, end, rowTotal);
		return parsingResult;
	}

	private void readRows(final Upload currentUpload,
			final ParsingResponse<Events> parsingResult, final Sheet sheet, final int ini, final int end,
			final int rowTotal) {
		
		List<Events> eventsToProcess = new ArrayList<Events>();
		final Iterator<Row> rowIterator = sheet.rowIterator();
		Row row = rowIterator.next();
		int rowNumber = 0;
		int index = 0;
		int batch_size = (end - ini) / 3;

		if (batch_size > rowTotal) {
			batch_size = rowTotal - 1;
		}

		for (int i = ini; i < end; i++) {
			rowNumber++;
			index++;
			row = sheet.getRow(i);

			try {
				final Events events = createEventObject(row);
				parsingResult.addValidObject(events);
				eventsToProcess.add(events);

				if (index >= batch_size) {
					eventDAO.createEvents(eventsToProcess);
					eventsToProcess = new ArrayList<Events>();
					index = 0;
					updateProgress(currentUpload, currentUpload.getUploadStatus() + 5);
				}
			} catch (FieldNotValidException e) {
				parsingResult.addInvalidRow(new InvalidRow(rowNumber, e.getMessage()));
			}
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void updateProgress(final Upload currentUpload, final int percent) {
		currentUpload.setUploadStatus(percent);
		final Upload newobject = uploadDAO.getUploadByRef(currentUpload.getUploadID());
		if (newobject.getUploadStatus() < percent) {
			newobject.setUploadStatus(percent);
			uploadDAO.update(newobject);
		}
	}

	private boolean isValidIMSI(final String imsi) {
		if (imsi == null || imsi.length() < 14 || imsi.length() > 15 ) {
			return false;
		}

		for (int i = 0; i < imsi.length(); i++) {
			if (!Character.isDigit(imsi.charAt(0))) {
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

	@Override
	public List<UniqueIMSI> findIMSISBetweenDates(final LocalDateTime startTime, final LocalDateTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new InvalidDateException();
		}

		return eventDAO.findIMSISBetweenDates(startTime, endTime);
	}

	@Override
	public List<UniqueIMSI> findIMSIS() {
		return eventDAO.findIMSIS().stream().sorted(Comparator.comparing(UniqueIMSI::getImsi))
				.collect(Collectors.toList());
	}

	@Override
	public List<DeviceCombination> findTopTenEvents(final LocalDateTime startTime, final LocalDateTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new InvalidDateException();
		}
		return eventDAO.findTopTenCombinations(startTime, endTime);
	}

	@Override
	public List<IMSICount> findIMSIS(final int number, final LocalDateTime startTime, final LocalDateTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new InvalidDateException();
		}
		return eventDAO.findIMSIS(number, startTime, endTime);
	}

	@Override
	public List<UniqueIMSI> findIMSISByFailure(final int failureClass) {
		if (failureClass < 0) {
			throw new InvalidFailureClassException();
		}
		return eventDAO.findIMSISByFailureClass(failureClass);
	}

	@Override
	public List<Events> findListofIMSIEventsByDate(final String imsi, final LocalDateTime startTime,
			final LocalDateTime endTime) {
		if (!isValidIMSI(imsi)) {
			throw new InvalidIMSIException();
		}
		return eventDAO.findAllIMSIEventsByDate(imsi, startTime, endTime);
	}

	@Override
	public List<Events> findListofIMSIEventsByMarketOperatorCellID(final int cellID, final String country, final String operator) {
		return eventDAO.findAllEventsByMarketOperatorCellID(cellID, country, operator);
	}

	@Override
	public List<Events> findListOfEventsByDescription(final String description) {
		return eventDAO.findAllEventsByFailureDescription(description);
	}

	@Override
	public List<Events> findListofIMSIEventsByPhoneModel(final String model, final LocalDateTime startTime, final LocalDateTime endTime)
			throws InvalidPhoneModelException {
		if (model.isEmpty()) {
			throw new InvalidPhoneModelException();
		}else if (startTime.isAfter(endTime)) {
			throw new InvalidDateException();
		}
		return eventDAO.findAllIMSIEventsByPhoneModel(model, startTime, endTime);
	}

}