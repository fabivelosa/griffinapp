package com.callfailures.services;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.callfailures.entity.FailureClass;

@Local
public interface FailureClassService {

	FailureClass findById(final int id);

	void create(FailureClass obj);
	
	 Map<String,List<FailureClass>> read(File workbookFile);
}
