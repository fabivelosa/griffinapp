package com.callfailures.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "ue")
@XmlRootElement
@Table(name = "ue")
public class UE {

	@Id
	private int ueType;
	private String marketingName;
	private String manufacturer;
	private int accessCapability;

	public int getUeType() {
		return ueType;
	}

	public void setUeType(int ueType) {
		this.ueType = ueType;
	}

	public String getMarketingName() {
		return marketingName;
	}

	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getAccessCapability() {
		return accessCapability;
	}

	public void setAccessCapability(int accessCapability) {
		this.accessCapability = accessCapability;
	}

}
