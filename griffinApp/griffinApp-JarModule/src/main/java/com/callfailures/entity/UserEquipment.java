package com.callfailures.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "ue")
@XmlRootElement
@Table(name = "ue")
public class UserEquipment {

	@Id
	private int ueType;
	private String marketingName;
	private String manufacturer;
	private String accessCapability;
	private String model;
	private String vendorName;
	private String deviceType;
	private String deviceOS;
	private String inputModel;

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

	public String getAccessCapability() {
		return accessCapability;
	}

	public void setAccessCapability(String accessCapability) {
		this.accessCapability = accessCapability;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	public String getInputModel() {
		return inputModel;
	}

	public void setInputModel(String inputModel) {
		this.inputModel = inputModel;
	}

}
