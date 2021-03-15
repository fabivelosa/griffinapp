package com.callfailures.services;

import java.io.File;
import java.util.List;

import javax.ejb.Local;

import com.callfailures.entity.UserEquipment;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface UserEquipmentService {

	UserEquipment findById(final int tac);

	void create(UserEquipment obj);
	
	ParsingResponse<UserEquipment> read(File workbookFile);
	
	List<UserEquipment> findAll();
}
