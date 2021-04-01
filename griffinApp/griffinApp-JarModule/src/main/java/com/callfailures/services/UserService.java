package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.User;

@Local
public interface UserService {

	User getUserById(String userId);

	void addUser(User user);

	User getUserByName(String username);

}
