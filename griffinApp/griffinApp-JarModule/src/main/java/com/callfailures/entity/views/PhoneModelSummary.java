package com.callfailures.entity.views;


import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity(name = "phoneModelSummary")
@Immutable

public class PhoneModelSummary {
	
	@Id
	private String model;
	private long callFailuresCount;
	
	

	public long getCallFailuresCount() {
		return callFailuresCount;
	}
	
	public String getModel() {
		return model;
	}

	public PhoneModelSummary() {
	}

	public PhoneModelSummary(final String model, final long callFailuresCount) {
		this.model=model;
		this.callFailuresCount = callFailuresCount;
		
	}

	

	


}




