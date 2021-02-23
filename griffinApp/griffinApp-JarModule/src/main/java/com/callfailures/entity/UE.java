package com.callfailures.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "ue")
@XmlRootElement
@Table(name = "ue")
public class UE {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ueId; // do we need it???
	private int typeAllocationCode;
	private String marketingName;
	private String manufacturer;
	private int accessCapability;

	public int getUeId() {
		return ueId;
	}

	public void setUeId(int ueId) {
		this.ueId = ueId;
	}

	public int getTypeAllocationCode() {
		return typeAllocationCode;
	}

	public void setTypeAllocationCode(int typeAllocationCode) {
		this.typeAllocationCode = typeAllocationCode;
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
