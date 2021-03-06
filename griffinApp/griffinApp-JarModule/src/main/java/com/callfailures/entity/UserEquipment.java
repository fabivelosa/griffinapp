package com.callfailures.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "ue")
@XmlRootElement
@Table(name = "ue")
@Cacheable
public class UserEquipment {

	@Id
	private int tac;
	private String model;
	private String vendorName;
	private String accessCapability;
	private String deviceType;
	private String ueType;
	private String deviceOS;
	private String inputMode;

	public UserEquipment() {}
	
	public UserEquipment(final int tac) {
		this.tac = tac;
	}

	public int getTac() {
		return tac;
	}

	public void setTac(final int tac) {
		this.tac = tac;

	}

	public String getModel() {
		return model;
	}

	public void setModel(final String model) {
		this.model = model;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(final String vendorName) {
		this.vendorName = vendorName;
	}

	public String getAccessCapability() {
		return accessCapability;
	}

	public void setAccessCapability(final String accessCapability) {
		this.accessCapability = accessCapability;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(final String deviceType) {
		this.deviceType = deviceType;
	}

	public String getUeType() {
		return ueType;
	}

	public void setUeType(final String ueType) {
		this.ueType = ueType;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(final String deviceOS) {
		this.deviceOS = deviceOS;
	}

	public String getInputMode() {
		return inputMode;
	}

	public void setInputMode(final String inputMode) {
		this.inputMode = inputMode;
	}
	
}
