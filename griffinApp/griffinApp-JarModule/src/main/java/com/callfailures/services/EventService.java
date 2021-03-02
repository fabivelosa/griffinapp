package com.callfailures.services;

import java.io.File;
import java.util.List;

import javax.ejb.Local;

import com.callfailures.entity.Events;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface EventService {

	Events findById(final int eventId);

	ParsingResponse<Events> read(File workbookFile);

}
