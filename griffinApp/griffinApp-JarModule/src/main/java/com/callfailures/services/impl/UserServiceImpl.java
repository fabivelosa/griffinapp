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
	public Boolean validateUser(String username, String password) {
		User user = userDao.getUserByName(username);
		
		if (user.getUserPassword().equals(password)) {
			   System.out.println(user.getUserName());
			return Boolean.TRUE;
			
		}

		return Boolean.FALSE;

	}

	@Override
	public User getUserById(String id) {
		return userDao.getUserById(id);
	}

	@Override
	public void addUser(User user) {
		userDao.addUser(user);

	}

	@Override
	public void updateUser(User user) {
		userDao.updateUser(user);

	}

}
