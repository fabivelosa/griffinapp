package com.callfailures.services;

import java.io.File;

import javax.ejb.Local;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface EventCauseService {

	EventCause findById(final EventCausePK eventcausePK);

	void create(EventCause obj);
	
	 ParsingResponse<EventCause> read(File workbookFile);
}
