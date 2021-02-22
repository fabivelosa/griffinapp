package com.callfailures.resources;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import java.util.Collection;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import com.callFailures.entity.CallFailure;
import com.callFailures.resources.CallFailureResource;
import javax.ws.rs.core.Response;


class CallFailureResourceTest {
	
	private final CallFailureResource callFailureResource= mock(CallFailureResource.class);
	private final Response importCallFailuresResponse = mock(Response.class);
	private CallFailure callFailure_1 ;
	private CallFailure callFailure_2; 


	@BeforeEach
	void setUp() throws Exception {
		
		 callFailure_1 = new CallFailure();
		 callFailure_2 = new CallFailure();
	
	}
	
	@Test
	void testForSuccessImportCallFailures() {
		
		final Collection<CallFailure> callFailures = new HashSet<CallFailure>();
		callFailure_1.setCellID(12);
		callFailure_1.setEventID(1234);
		callFailures.add(callFailure_1);
		callFailures.add(callFailure_2);
		when(callFailureResource.importCallFailures(callFailures)).thenReturn(importCallFailuresResponse);
		callFailureResource.importCallFailures(callFailures);
		verify(callFailureResource, new Times(1)).importCallFailures(callFailures);
	
	}

}
