package com.rtcsoft.sevakendra.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

	private final Set<String> blacklistedTokens = new HashSet<>();

	public void addTokenToBlacklist(String token) {
		blacklistedTokens.add(token);
	}

	public boolean isTokenBlacklisted(String token) {
		return blacklistedTokens.contains(token);
	}
}
