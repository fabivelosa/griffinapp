package com.callfailures.commontests.utils;

public enum ResourceDefinitions {
	USERS("users"),
	YOGA_CLASSES("yoga-classes");
	
	private final String resourceName;

	private ResourceDefinitions(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}
	
	
}
