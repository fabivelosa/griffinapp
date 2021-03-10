package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.User;

@Local
public interface UserService {

	Boolean validateUser(final String username, String password);

	User getUserById(String userId);

	void addUser(User user);

	void updateUser(User user);

}
