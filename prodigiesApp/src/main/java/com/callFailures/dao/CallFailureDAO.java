package com.callFailures.dao;

import java.util.Collection;
import com.callFailures.entity.CallFailure;

public interface CallFailureDAO {

	void save(Collection<CallFailure> callFailures);
}
