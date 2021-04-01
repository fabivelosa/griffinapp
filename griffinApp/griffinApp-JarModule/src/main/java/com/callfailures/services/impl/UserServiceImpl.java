package com.callfailures.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.callfailures.dao.UsersDAO;
import com.callfailures.entity.User;
import com.callfailures.services.UserService;

@Stateless
public class UserServiceImpl implements UserService {

	@Inject
	UsersDAO userDao;

	
	@Override
	public User getUserById(final String UserId) {
		return userDao.getUserByUserId(UserId);
	}
	
	@Override
	public User getUserByName(final String username) {
		return userDao.getUserByName(username);
	}

	@Override
	public void addUser(final User user) {
		userDao.addUser(user);
	}

}
