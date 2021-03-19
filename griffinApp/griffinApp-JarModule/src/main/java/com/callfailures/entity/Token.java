package com.callfailures.entity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Token {

	private static final Map<String, User> TOKEN_HOLDER = new ConcurrentHashMap<>();

	public static User issueNewToken(User auth) {
		String token = UUID.randomUUID().toString();
		auth.setToken(token);
		TOKEN_HOLDER.put(token, auth);
		return auth;
	}

	public static boolean validateToken(String token) {
		User authenticationInfo = TOKEN_HOLDER.get(token);
		if (authenticationInfo != null) {
			return true;
		} else {
			return false;
		}
	}

	public static void revokeToken(String token) {
		TOKEN_HOLDER.remove(token);
	}
}