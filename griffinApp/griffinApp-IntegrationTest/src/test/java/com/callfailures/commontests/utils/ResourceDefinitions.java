package com.callfailures.commontests.utils;

public enum ResourceDefinitions {
	EVENTS("events");
	
	private final String resourceName;

	private ResourceDefinitions(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}
	
	
}
