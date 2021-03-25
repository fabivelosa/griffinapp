package com.callfailures.entity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Singleton;

@Singleton
public class TokenUtil {

	private static final Map<String, User> TOKEN_HOLDER = new ConcurrentHashMap<>();

	public static User issueNewToken(final User auth) {
		final String token = UUID.randomUUID().toString();
		auth.setToken(token);
		TOKEN_HOLDER.put(token, auth);
		return auth;
	}

	public static boolean validateToken(final String token) {

		String tokenId = token;
		if (token.startsWith("Bearer ")) {
			tokenId = token.substring(7, token.length());
		}

		final User authenticationInfo = TOKEN_HOLDER.get(tokenId);
		
		return authenticationInfo != null;

	}

	public static void revokeToken(final String token) {
		TOKEN_HOLDER.remove(token);
	}
}