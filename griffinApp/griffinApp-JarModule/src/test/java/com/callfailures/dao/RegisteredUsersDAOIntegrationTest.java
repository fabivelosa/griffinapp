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
	public class RegisteredUsersDAOIntegrationTest {
		   private User user = new User();
		   private final UserGenerator userGenerator = new UserGenerator();
		   private static final String userId = "A1";
		   private static final String userName = "John Doe";
		   private static final String userType = "Network Manager";
		   private static final String userPassword = "password";
		
		   @Deployment
		   public static JavaArchive createTestArchive() {
		      return ShrinkWrap.create(JavaArchive.class, "test.jar")
		         .addClasses(User.class, UsersDAO.class, UserGenerator.class)
		         .addAsManifestResource(
		            "arquillian-persistence/persistence.xml", 
		            ArchivePaths.create("persistence.xml"));
		   }

		   @Inject
		   private UsersDAO registeredUsersDAO;
		   
		   @Test
		   public void testRun() {
			   System.out.println("I am running!!!");
		   }
		   
		   
//		   @Test
//		   public void testCreate() {
//			   user.setUserId(userId);
//			   user.setUserName(userName);
//			   user.setUserType(userType);
//			   user.setUserPassword(userPassword);
//			   registeredUsersDAO.addUser(user);
//		   }
//
//		   @Test
//		   public void testGetUserByIdSuccess() {
//			   user.setUserId(userId);
//			   user.setUserName(userName);
//			   user.setUserType(userType);
//			   user.setUserPassword(userPassword);
//			   registeredUsersDAO.addUser(user);
//			   final User queryResult = registeredUsersDAO.getUserById(userId);
//			   assertEquals(userId, queryResult.getUserId());
//		   }
		   
//		   @Test
//		   public void testGetUserByIdFailure() {
//			   final User queryResult = registeredUsersDAO.getUserById(null);
//			   assertEquals(userId, queryResult.getUserId());
//		   }
		   
	}
