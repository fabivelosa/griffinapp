package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.User;

@Local
public interface UserService {

	Boolean validateUser(final String username, String password);
	
	public User getUserById(String id);
		

	public void addUser(User user);
		

	public void updateUser(User user);
	


}
