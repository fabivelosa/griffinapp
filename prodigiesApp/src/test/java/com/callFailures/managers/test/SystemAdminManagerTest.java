package com.callFailures.managers.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import com.callFailures.entity.CallFailure;
import com.callFailures.managers.SystemAdminManager;

class SystemAdminManagerTest {
	
	private final SystemAdminManager systemAdminManager= mock(SystemAdminManager.class);
	private CallFailure callFailure_1 ;
	private CallFailure callFailure_2; 
	
	

	@BeforeEach
	void setUp() throws Exception {
		 callFailure_1 = new CallFailure();
		 callFailure_2 = new CallFailure();
		
	}

	
	@Test
	void testForSaveCallFailures() {
		final Collection<CallFailure> callFailures = new HashSet<CallFailure>();
		callFailures.add(callFailure_1);
		callFailures.add(callFailure_2);
		final ArrayList<CallFailure> callFailureList =new ArrayList<CallFailure>();
		callFailureList.add(callFailure_1);
		callFailureList.add(callFailure_2);
		when(systemAdminManager.saveCallFailures(callFailures)).thenReturn(callFailureList);
		systemAdminManager.saveCallFailures(callFailures);
		verify(systemAdminManager,new Times(1)).saveCallFailures(callFailures);
		}

	@Test
	void testSuccessForNoOfCallFailuresAdded() {
		final Collection<CallFailure> callFailures = new HashSet<CallFailure>();
		callFailures.add(callFailure_1);
		callFailures.add(callFailure_2);
		final ArrayList<CallFailure> callFailureList =new ArrayList<CallFailure>();
		callFailureList.add(callFailure_1);
		callFailureList.add(callFailure_2);
		final int noOfCallFailuresSaved = callFailures.size();
		final int noOfCallFailuresReturned= callFailureList.size();
		when(systemAdminManager.saveCallFailures(callFailures)).thenReturn(callFailureList);
		systemAdminManager.saveCallFailures(callFailures);
		assertEquals(noOfCallFailuresSaved ,noOfCallFailuresReturned);
		verify(systemAdminManager,new Times(1)).saveCallFailures(callFailures);
		}
				
		@Test
		void testFailForNoOfCallFailuresAdded() {
			final Collection<CallFailure> callFailures = new HashSet<CallFailure>();
			callFailures.add(callFailure_1);
			callFailures.add(callFailure_2);
			final ArrayList<CallFailure> callFailureList =new ArrayList<CallFailure>();
			callFailureList.add(callFailure_1);
			final int noOfCallFailuresSaved = callFailures.size();
			final int noOfCallFailuresReturned= callFailureList.size();
			when(systemAdminManager.saveCallFailures(callFailures)).thenReturn(callFailureList);
			systemAdminManager.saveCallFailures(callFailures);
			assertNotEquals(noOfCallFailuresSaved ,noOfCallFailuresReturned);
			verify(systemAdminManager,new Times(1)).saveCallFailures(callFailures);
			}			
	
		
}
