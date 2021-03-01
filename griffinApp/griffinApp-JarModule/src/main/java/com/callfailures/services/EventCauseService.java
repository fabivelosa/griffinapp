package com.callfailures.services;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.FailureClass;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface EventCauseService {

	EventCause findById(final EventCausePK id);

	void create(EventCause obj);
	
	 ParsingResponse<EventCause> read(File workbookFile);
}
