package com.callFailures.resources;

import java.util.Collection;
import javax.ws.rs.core.Response;
import com.callFailures.entity.CallFailure;



public interface CallFailureResource {

    Response importCallFailures(Collection<CallFailure> callFailures) ;

}