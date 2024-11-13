package com.rtcsoft.sevakendra.services;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;

@Service
public class SharedService {

	public boolean checkAccessBySessionAtrribute(@NonNull HttpServletRequest request) {
		Long authUserId = getUserIdFromHeader(request);
		Long sessionUserId = getUserIdFromSession(request);
		System.out.println("Checking session");
		return authUserId.equals(sessionUserId);
	}

	public long getUserIdFromHeader(HttpServletRequest request) {
		String userIdHeader = request.getHeader("userId"); // Get the header as a String

		if (userIdHeader != null) {
			try {
				return Long.parseLong(userIdHeader); // Convert to long
			} catch (NumberFormatException e) {
				System.out.println("Invalid userId format: " + userIdHeader);
				// Handle error as needed (e.g., throw exception, return default, etc.)
			}
		}

		// Handle the case where userId header is missing or invalid
		return -1L; // Or any default value you wish to use
	}

	public long getUserIdFromSession(HttpServletRequest request) {
		Object userIdAttribute = request.getSession().getAttribute("userId");

		if (userIdAttribute instanceof Long) {
			return (Long) userIdAttribute; // Cast directly if it's already a Long
		} else if (userIdAttribute instanceof String) {
			try {
				return Long.parseLong((String) userIdAttribute); // Parse if it's a String
			} catch (NumberFormatException e) {
				System.out.println("Invalid userId format: " + userIdAttribute);
				// Handle parsing error (e.g., log or throw a custom exception)
			}
		}

		return -1L; // Or any default value to indicate missing or invalid userId
	}
}
