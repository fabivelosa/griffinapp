package com.callfailures.resource;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.callfailures.entity.Secured;
import com.callfailures.entity.TokenUtil;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@Override
	public void filter(final ContainerRequestContext requestContext) {
		final String authenticationToken = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (authenticationToken == null || !TokenUtil.validateToken(authenticationToken)) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}
}
