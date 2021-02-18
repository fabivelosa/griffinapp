package com.callFailures.managers;

import java.util.Collection;
import java.util.List;

import com.callFailures.entity.CallFailure;

public interface SystemAdminManager {
	
	List <CallFailure> saveCallFailures(Collection<CallFailure> callFailures);

}
