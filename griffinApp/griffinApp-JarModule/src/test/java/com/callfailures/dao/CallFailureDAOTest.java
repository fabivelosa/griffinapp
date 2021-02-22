package com.callfailures.dao;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.util.Collection;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import com.callFailures.dao.CallFailureDAO;
import com.callFailures.entity.CallFailure;

class CallFailureDAOTest {
	private final CallFailureDAO callFailureDAO= mock(CallFailureDAO.class);
	private CallFailure callFailure_1 ;
	private CallFailure callFailure_2; 
	
	@BeforeEach
	void setUp() throws Exception {
		 callFailure_1 = new CallFailure();
		 callFailure_2 = new CallFailure();
	}
	

	@Test
	void testForSaveCallFailureDAO() {
		
		final Collection<CallFailure> callFailures = new HashSet<CallFailure>();
		callFailures.add(callFailure_1);
		callFailures.add(callFailure_2);
		callFailureDAO.save(callFailures);
		verify(callFailureDAO,new Times(1)).save(callFailures);
	}
}
