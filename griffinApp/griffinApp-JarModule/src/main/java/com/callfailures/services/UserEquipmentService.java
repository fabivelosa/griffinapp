package com.callfailures.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.callfailures.entity.UserEquipment;

@Local
public interface UserEquipmentService {

	UserEquipment findById(final int id);

	void create(UserEquipment obj);
	
	Map<String, List<UserEquipment>> read(File workbookFile);
}
