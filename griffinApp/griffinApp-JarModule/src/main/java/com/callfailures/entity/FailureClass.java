package com.callfailures.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "failureClass")
@XmlRootElement
@Table(name = "failureClass")
public class FailureClass {
	@Id
	@Min(0)
	@Max(4)
	private int failureClass;
	
	private String failureDesc;

	public FailureClass() {}
	
	public FailureClass(int failureClass, String failureDesc) {
		this.failureClass = failureClass;
		this.failureDesc = failureDesc; 
	}
	
	public int getFailureClass() {
		return failureClass;
	}

	public void setFailureClass(int failureClass) {
		this.failureClass = failureClass;
	}

	public String getFailureDesc() {
		return failureDesc;
	}

	public void setFailureDesc(String failureDesc) {
		this.failureDesc = failureDesc;
	}

}