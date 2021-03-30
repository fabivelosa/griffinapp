package com.callfailures.entity.views;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity(name = "imsiCount")
@Immutable
public class IMSICount {
	
	@Id
	private String imsi;
	
	private long callFailuresCount;
	
	public IMSICount() {
	}

	public IMSICount(final String imsi, final long callFailuresCount) {
		this.imsi = imsi;
		this.callFailuresCount = callFailuresCount;
	}

	public String getImsi() {
		return imsi;
	}

	public long getCallFailuresCount() {
		return callFailuresCount;
	}
	
}
