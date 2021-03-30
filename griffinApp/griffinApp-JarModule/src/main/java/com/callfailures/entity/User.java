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

	public static String passDecrypt(final String extract) {
		final byte[] sDecode = Base64.getDecoder().decode(extract);
		final String result = new String(sDecode);
		return result;
	}

	public static String passEncrypt(final String entry) {
		final byte[] sBytes = entry.getBytes();
		final String encoded = Base64.getEncoder().encodeToString(sBytes);
		return encoded;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(final String userPassword) {
		this.userPassword = userPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}
}
