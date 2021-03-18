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
	public Boolean validateUser(final User user) {
		final User userDb = userDao.getUserByName(user.getUserName());

		if (userDb.getUserPassword().equals(user.getUserPassword())) {
			System.out.println(user.getUserName());
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public User getUserById(final String UserId) {
		return userDao.getUserByUserId(UserId);
	}

	@Override
	public void addUser(final User user) {
		userDao.addUser(user);
	}

	@Override
	public void updateUser(final User user) {
		userDao.updateUser(user);
	}

}
