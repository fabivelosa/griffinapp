package com.callfailures.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Base64;



@Entity(name = "users")
@XmlRootElement
@Table(name = "users")
public class User {

	
	@Id
	private String userId;
	private String userName;
	private String userType;
	private String userPassword;
	private String token;

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(final String userType) {
		this.userType = userType;
	}
	
	public String passDecrypt(String extract) {
		byte[] sDecode = Base64.getDecoder().decode(extract);
		String result = new String(sDecode);
		return result;
	}

	public String getUserPassword() {
		String demystify = passDecrypt(userPassword);
		return demystify;
	}
	
	public String passEncrypt(final String entry) {
		byte[] sBytes = entry.getBytes();
		String encoded = Base64.getEncoder().encodeToString(sBytes);
		return encoded;
	}

	public void setUserPassword(final String userPassword) {
		this.userPassword = passEncrypt(userPassword);
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}
}
