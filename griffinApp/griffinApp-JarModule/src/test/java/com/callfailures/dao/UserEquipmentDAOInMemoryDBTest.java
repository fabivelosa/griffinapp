package com.callfailures.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.callfailures.entity.UserEquipment;
import com.callfailures.utils.test.DBCommandTransactionalExecutor;

public class UserEquipmentDAOInMemoryDBTest {
	private EntityManagerFactory emf;
	private EntityManager entityManager;
	private UserEquipmentDAO userEquipmentDaO;
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;
	private final UserEquipment userEquipment = new UserEquipment();
	// private final EntityGenerator entityGenerator = new EntityGenerator();
	private static final int tac = 21060800;
	private static final String modelName = "VEA3";
	private static final String vendorName = "S.A.R.L. B  & B International";
	private static final String accessCapability = "GSM 1800, GSM 900";

	@BeforeEach
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("eventsInMemoryPU");
		entityManager = emf.createEntityManager();

		userEquipmentDaO = new UserEquipmentDAO();
		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(entityManager);
		userEquipmentDaO.entityManager = entityManager;

		dBCommandTransactionalExecutor.executeCommand(() -> {
			userEquipment.setTac(tac);
			userEquipment.setModel(modelName);
			userEquipment.setVendorName(vendorName);
			userEquipment.setAccessCapability(accessCapability);
			userEquipmentDaO.create(userEquipment);
			return null;
		});
	}

	@Test
	public void testGetUEId() {
		final UserEquipment queryResult = userEquipmentDaO.getUserEquipment(tac);
		assertEquals(tac, queryResult.getTac());
		assertEquals(modelName, queryResult.getModel());
		assertEquals(vendorName, queryResult.getVendorName());
		assertEquals(accessCapability, queryResult.getAccessCapability());
	}

	@Test
	public void testFailureGetUENullTac() {
		assertEquals(null, userEquipmentDaO.getUserEquipment(3));
	}

	@Test
	public void testFailureGetAllUserEuipment() {
		final List<UserEquipment> userEquipment = userEquipmentDaO.findAll();
		assertEquals(1, userEquipment.size());
	}

}