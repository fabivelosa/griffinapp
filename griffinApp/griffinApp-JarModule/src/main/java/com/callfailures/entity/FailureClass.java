package com.callfailures.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "failureClass")
@XmlRootElement
@Table(name = "failureClass")
public class FailureClass {
	@Id
	private int failureClass;
	private Date failureDesc;

	public int getFailureClass() {
		return failureClass;
	}

	public void setFailureClass(int failureClass) {
		this.failureClass = failureClass;
	}

	public Date getFailureDesc() {
		return failureDesc;
	}

	public void setFailureDesc(Date failureDesc) {
		this.failureDesc = failureDesc;
	}

}