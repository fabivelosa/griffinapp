package com.callfailures.entity.views;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity(name = "imsiSummary")
@Immutable
public class IMSISummary {
	
	@Id
	private String imsi;
	
	private long callFailuresCount;
	
	private long totalDurationMs;

	public IMSISummary() {
	}

	public IMSISummary(String imsi, long callFailuresCount, long totalDurationMs) {
		this.imsi = imsi;
		this.callFailuresCount = callFailuresCount;
		this.totalDurationMs = totalDurationMs;
	}

	public String getImsi() {
		return imsi;
	}

	public long getCallFailuresCount() {
		return callFailuresCount;
	}
	
	public long getTotalDurationMs() {
		return totalDurationMs;
	}
}
