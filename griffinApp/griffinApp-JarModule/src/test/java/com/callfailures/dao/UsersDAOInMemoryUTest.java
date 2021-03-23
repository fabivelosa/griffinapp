package com.callfailures.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.callfailures.entity.User;
import com.callfailures.entity.UserEquipment;
import com.callfailures.utils.test.DBCommandTransactionalExecutor;

class UsersDAOInMemoryUTest {
	private EntityManagerFactory emf;
	private EntityManager entityManager;
	private final UsersDAO usersDAO = new UsersDAO();
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;

	@BeforeEach
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("eventsInMemoryPU");
		entityManager = emf.createEntityManager();
		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(entityManager);
		usersDAO.entityManager = entityManager;

	}

	// one
	@Test
	void testAddUser() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			final User user = new User();
			user.setUserId("A100");
			user.setUserName("wilmir");
			user.setUserPassword("password");
			user.setUserType("network engineer");
			usersDAO.addUser(user);
			return null;
		});

	}

	// two
	@Test
	void testGetUserByName() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			final User user = new User();
			user.setUserId("A100");
			user.setUserName("wilmir");
			user.setUserPassword("password");
			user.setUserType("network engineer");
			usersDAO.addUser(user);
			usersDAO.getUserByName("wilmir");
			return user;
		});
	}

	// three
	@Test
	void testGetUserByUserId() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			final User user = new User();
			user.setUserId("A100");
			user.setUserName("wilmir");
			user.setUserPassword("password");
			user.setUserType("network engineer");
			usersDAO.addUser(user);
			usersDAO.getUserByUserId("A100");

			return user;
		});
	}

	// four
	@Test
	void testGetRegisteredUsers() {
		dBCommandTransactionalExecutor.executeCommand(() -> {

			final User user1 = new User();
			user1.setUserId("A1");
			user1.setUserName("example1");
			user1.setUserPassword("access");
			user1.setUserType("cust. service");
			usersDAO.addUser(user1);

			final User user2 = new User();
			user2.setUserId("A2");
			user2.setUserName("example1");
			user2.setUserPassword("access");
			user2.setUserType("cust. service");
			usersDAO.addUser(user2);

			final List<User> users = usersDAO.getRegisteredUsers();
			return users;
		});
	}

	@Test
	void testUpdateUser() {

		dBCommandTransactionalExecutor.executeCommand(() -> {

			final User user = new User();
			user.setUserId("A100");
			user.setUserName("wilmir");
			user.setUserPassword("password");
			user.setUserType("network engineer");
			usersDAO.addUser(user);
			user.setUserName("My Boo");
			usersDAO.updateUser(user);
			assertEquals("A100", user.getUserId());
			assertEquals("My Boo", user.getUserName());
			assertEquals("password", user.getUserPassword());
			assertEquals("network engineer", user.getUserType());
			return user;
			
		});
	}
	
	
}
