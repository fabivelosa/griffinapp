package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.UserEquipment;

@Local
public interface UserEquipmentService {

	UserEquipment findById(final int id);

	void create(UserEquipment obj);

}
