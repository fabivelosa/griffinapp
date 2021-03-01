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
import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.entity.UserEquipment;
import com.callfailures.utils.test.EntityGenerator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Arquillian.class)
public class UserEqipmentDAOIntegrationTest {
	   @Deployment
	   public static JavaArchive createTestArchive() {
	      return ShrinkWrap.create(JavaArchive.class, "test.jar")
	         .addClasses(UserEquipmentDAO.class, Events.class, EventCause.class, 
	        		 MarketOperator.class, UserEquipment.class, FailureClass.class,
	        		 EventCausePK.class, MarketOperatorPK.class, EntityGenerator.class)
	         .addAsManifestResource(
	            "arquillian-persistence/persistence.xml", 
	            ArchivePaths.create("persistence.xml"));
	   }


	   @Inject
	   private UserEquipmentDAO userEquipmentDAO;
	   
	   private UserEquipment userEquipment = new UserEquipment();
	   private final EntityGenerator entityGenerator = new EntityGenerator();
	   private static final int ueId = 21060800;
	   private static final String accessCapability = "GSM 1800, GSM 900";
	   
	   @Test
	   public void testCreate() {
		   userEquipment = entityGenerator.parseUE(ueId);
		   userEquipment.setAccessCapability(accessCapability);
		   userEquipmentDAO.create(userEquipment);
	   }

	   @Test
	   public void testGetUEId() {
		   final UserEquipment queryResult = userEquipmentDAO.getUserEquipment(ueId);
		   assertEquals(ueId, queryResult.getTac());
	   }
	   
	   @Test
	   public void testGetUEAccessCapability() {
		   final UserEquipment queryResult = userEquipmentDAO.getUserEquipment(ueId);
		   assertEquals(accessCapability, queryResult.getAccessCapability());
	   }
	   
	   @Test
	   public void testFailureGetUE() {
		   assertEquals(null, userEquipmentDAO.getUserEquipment(3));
	   }
   
	}