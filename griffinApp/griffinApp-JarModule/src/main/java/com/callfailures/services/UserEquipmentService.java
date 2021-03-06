package com.callfailures.services;

import java.io.File;

import javax.ejb.Local;

import com.callfailures.entity.UserEquipment;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface UserEquipmentService {

	UserEquipment findById(final int userEquipment);

	void create(UserEquipment obj);
	
	ParsingResponse<UserEquipment> read(File workbookFile);
}
