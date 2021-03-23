package com.callfailures.entity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Singleton;

@Singleton
public class Token {

	private static final Map<String, User> TOKEN_HOLDER = new ConcurrentHashMap<>();

	public static User issueNewToken(User auth) {
		String token = UUID.randomUUID().toString();
		auth.setToken(token);
		TOKEN_HOLDER.put(token, auth);
		return auth;
	}

	public static boolean validateToken(String token) {

		String tokenId = token;
		if (token.startsWith("Bearer ")) {
			tokenId = token.substring(7, token.length());
		}

		User authenticationInfo = TOKEN_HOLDER.get(tokenId);
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