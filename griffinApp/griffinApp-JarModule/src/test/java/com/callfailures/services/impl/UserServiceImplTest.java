package com.callfailures.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.callfailures.dao.UsersDAO;
import com.callfailures.entity.User;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	private UsersDAO userDao = mock(UsersDAO.class);
	private User user;
	private String userId = "SA-13";
	private String username = "Meghan Markle";
	
	
	@InjectMocks
	private UserServiceImpl service;
	
	@Before
	public void setup() throws Exception{
		
		user = new User();
		user.setUserName(username);
		user.setUserId(userId);
		
	}
	
	@Test
	public void testGetUserByIdSuccess() {
		
		when(userDao.getUserByUserId(userId)).thenReturn(user);		
		String one = (service.getUserById(userId)).getUserId();
		verify(userDao, new Times(1)).getUserByUserId(anyString());
		assertEquals(one, "SA-13");			
	}
	
	@Test
	public void testGetUserByIdFailure() {
		
		when(userDao.getUserByUserId(userId)).thenReturn(null);
		service.getUserById(userId);
		verify(userDao, new Times(1)).getUserByUserId(anyString());
		assertNotEquals(user, service.getUserById(userId));			
	}
	
	@Test
	public void testGetUserByNameSuccess() {
		
		when(userDao.getUserByName(username)).thenReturn(user);		
		String one = (service.getUserByName(username)).getUserName();
		verify(userDao, new Times(1)).getUserByName(anyString());
		assertEquals(one, "Meghan Markle");			
	}
	
	@Test
	public void testGetUserByNameFailure() {
		
		when(userDao.getUserByName(username)).thenReturn(null);
		service.getUserByName(username);
		verify(userDao, new Times(1)).getUserByName(anyString());
		assertNotEquals(user, service.getUserByName(username));
	}
	
	@Test
	public void testAddUserSuccess() {
		Mockito.doNothing().when(userDao).addUser(user);
		service.addUser(user);
		verify(userDao, new Times(1)).addUser(user);
	}
}
