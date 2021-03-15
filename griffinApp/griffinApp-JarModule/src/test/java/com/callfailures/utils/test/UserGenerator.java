package com.callfailures.utils.test;

import com.callfailures.entity.User;

public class UserGenerator {

	public User generateUserInstance(final String userId, final String userName, final String userType, final String userPassword) {

		User user = new User();
		user.setUserId(userId);
		user.setUserName(userName);
		user.setUserType(userType);
		user.setUserPassword(userPassword);
		return user;
	}

}