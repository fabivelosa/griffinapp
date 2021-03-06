package com.callfailures.services;

import java.io.File;

import javax.ejb.Local;

import com.callfailures.entity.Events;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface EventService {

	ParsingResponse<Events> read(File workbookFile);

}
