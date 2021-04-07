package com.callfailures.services;

import java.util.List;

import javax.ejb.Local;

import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.entity.UserEquipment;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface UserEquipmentService {

	UserEquipment findById(final int tac);

	void create(UserEquipment obj);
	
	ParsingResponse<UserEquipment> read(final Workbook workbook);
	
	List<UserEquipment> findAll();
}
