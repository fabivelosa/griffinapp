package com.callfailures.services;

import java.io.File;
import java.util.List;

import javax.ejb.Local;

import com.callfailures.entity.FailureClass;

@Local
public interface FailureClassService {

	public FailureClass findById(final int id);

	public void create(FailureClass obj);
	
	public List<FailureClass> read(File workbookFile);
}
