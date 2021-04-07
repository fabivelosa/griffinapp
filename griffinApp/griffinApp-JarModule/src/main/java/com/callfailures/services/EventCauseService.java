package com.callfailures.services;

import javax.ejb.Local;

import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface EventCauseService {

	EventCause findById(final EventCausePK eventcausePK);

	void create(EventCause obj);

	ParsingResponse<EventCause> read(final Workbook workbook);
}
