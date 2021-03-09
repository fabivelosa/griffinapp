package com.callfailures.dao;


	import static org.junit.Assert.*;
	import javax.inject.Inject;
	import org.junit.runners.MethodSorters;
	import org.jboss.arquillian.container.test.api.Deployment;
	import org.jboss.arquillian.junit.Arquillian;
	import org.jboss.shrinkwrap.api.ArchivePaths;
	import org.jboss.shrinkwrap.api.ShrinkWrap;
	import org.jboss.shrinkwrap.api.spec.JavaArchive;
	import org.junit.FixMethodOrder;
	import org.junit.Test;
	import org.junit.runner.RunWith;

import com.callfailures.entity.User;
import com.callfailures.utils.test.UserGenerator;

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	@RunWith(Arquillian.class)
	public class RegisteredUsersDAOTest {
		   @Deployment
		   public static JavaArchive createTestArchive() {
		      return ShrinkWrap.create(JavaArchive.class, "test.jar")
		         .addClasses(RegisteredUsersDAO.class, UserGenerator.class)
		         .addAsManifestResource(
		            "arquillian-persistence/persistence.xml", 
		            ArchivePaths.create("persistence.xml"));
		   }


		   @Inject
		   private RegisteredUsersDAO registeredUsersDAO;
		   
		   private User user = new User();
		   private final UserGenerator userGenerator = new UserGenerator();
		   private static final String userId = "A1";
		   private static final String userName = "John Doe";
		   private static final String userType = "Network Manager";
		   private static final String userPassword = "password";
		   
		   @Test
		   public void testCreate() {
			   user = userGenerator.generateUserInstance(userId, userName, userType, userPassword);
			   registeredUsersDAO.addUser(user);
		   }

		   @Test
		   public void testGetUserByIdSuccess() {
			   final User queryResult = registeredUsersDAO.getUserById(userId);
			   assertEquals(userId, queryResult.getUserId());
		   }
		   
		   @Test
		   public void testGetUserByIdFailure() {
			   final User queryResult = registeredUsersDAO.getUserById(null);
			   assertEquals(userId, queryResult.getUserId());
		   }
		   
	}
