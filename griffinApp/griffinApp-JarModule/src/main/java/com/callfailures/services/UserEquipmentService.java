package com.callfailures.services;

import java.io.File;
import java.util.ArrayList;

import javax.ejb.Local;

import com.callfailures.entity.UserEquipment;

@Local
public interface UserEquipmentService {

	UserEquipment findById(final int id);

	void create(UserEquipment obj);
	
	ArrayList<UserEquipment> read(File workbookFile);
}
