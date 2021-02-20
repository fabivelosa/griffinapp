package com.callfailures.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "mccmnc")
@XmlRootElement
@Table(name = "mccmnc")
public class MobileCCNCCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mccmncId; // do we need it???
	private int mobileCountryCode;
	private int mobileNetworkCode;
	private String country;
	private String operator;

	public int getMccmncId() {
		return mccmncId;
	}

	public void setMccmncId(int mccmncId) {
		this.mccmncId = mccmncId;
	}

	public int getMobileCountryCode() {
		return mobileCountryCode;
	}

	public void setMobileCountryCode(int mobileCountryCode) {
		this.mobileCountryCode = mobileCountryCode;
	}

	public int getMobileNetworkCode() {
		return mobileNetworkCode;
	}

	public void setMobileNetworkCode(int mobileNetworkCode) {
		this.mobileNetworkCode = mobileNetworkCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
