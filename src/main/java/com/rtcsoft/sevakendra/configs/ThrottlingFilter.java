package com.rtcsoft.sevakendra.configs;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import java.io.IOException;

//import java.util.Map;

//import java.util.concurrent.ConcurrentHashMap;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebFilter("/*") // Apply to all endpoints
//public class ThrottlingFilter implements Filter {
//
//	private final Map<String, Long> clientLastRequestTime = new ConcurrentHashMap<>();
//	private static final long THROTTLE_INTERVAL = 1000; // 1 request per second
//	public static final int SC_TOO_MANY_REQUESTS = 429;
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//
//		if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
//
//			// Identify client (e.g., by IP address)
//			String clientIp = httpRequest.getRemoteAddr();
//			// String clientIdentifier = httpRequest.getHeader("Authorization");
//			long currentTime = System.currentTimeMillis();
//
//			clientLastRequestTime.putIfAbsent(clientIp, 0L);
//			long lastRequestTime = clientLastRequestTime.get(clientIp);
//			System.out.print("THROTTLE_TIME");
//			System.out.println(lastRequestTime);
//			if (currentTime - lastRequestTime < THROTTLE_INTERVAL) {
//				// Too many requests, send 429 response
//				httpResponse.setStatus(SC_TOO_MANY_REQUESTS);
//				httpResponse.getWriter().write("Too many requests. Please slow down.");
//				return;
//			}
//
//			// Update last request time
//			clientLastRequestTime.put(clientIp, currentTime);
//		}
//
//		// Continue processing the request
//		chain.doFilter(request, response);
//	}
//
//	@Override
//	public void init(FilterConfig filterConfig) {
//		// Optional: Initialize resources if needed
//	}
//
//	@Override
//	public void destroy() {
//		// Optional: Clean up resources if needed
//	}
//}

// MAX_REQUEST_CODE
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*") // Apply to all endpoints
public class ThrottlingFilter implements Filter {

	private final Map<String, UserRequestInfo> clientRequestInfo = new ConcurrentHashMap<>();
	private static final long TIME_WINDOW = 60000; // 1 minute (in milliseconds)
	private static final int MAX_REQUESTS = 60; // Allow max 60 requests per minute
	private static final int SC_TOO_MANY_REQUESTS = 429;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {

			String clientId = getClientIdentifier(httpRequest); // Get unique user ID
			long currentTime = System.currentTimeMillis();

			UserRequestInfo requestInfo = clientRequestInfo.getOrDefault(clientId, new UserRequestInfo());
			synchronized (requestInfo) {
				// Check if the time window has expired
				if (currentTime - requestInfo.startTime > TIME_WINDOW) {
					requestInfo.startTime = currentTime; // Reset time window
					requestInfo.requestCount = 0; // Reset request count
				}

				// Increment request count and check if it exceeds the limit
				requestInfo.requestCount++;
				if (requestInfo.requestCount > MAX_REQUESTS) {
					httpResponse.setStatus(SC_TOO_MANY_REQUESTS);
					httpResponse.getWriter().write("Too many requests. Please wait before retrying.");
					return;
				}

				// Update the map with the latest info
				clientRequestInfo.put(clientId, requestInfo);
			}
		}

		// Allow the request to proceed
		chain.doFilter(request, response);
	}

	private String getClientIdentifier(HttpServletRequest request) {
		// Example: Use IP address as identifier
		return request.getRemoteAddr();
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

	// Helper class to track request info
	private static class UserRequestInfo {
		long startTime = System.currentTimeMillis(); // When the time window started
		int requestCount = 0; // Number of requests in the current window
	}
}

// BLOCK_USER_CODE
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebFilter("/*") // Apply to all endpoints
//public class ThrottlingFilter implements Filter {
//
//	// Tracks the last request time for each user
//	private final Map<String, Long> clientLastRequestTime = new ConcurrentHashMap<>();
//	// Tracks the blocked status for each user
//	private final Map<String, Long> blockedClients = new ConcurrentHashMap<>();
//
//	private static final long THROTTLE_INTERVAL = 1000; // 1 request per second
//	private static final long BLOCK_DURATION = 60000; // Block for 1 minute (in milliseconds)
//	private static final int MAX_REQUESTS = 5; // Allow 5 requests per interval
//	private static final int SC_TOO_MANY_REQUESTS = 429;
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//
//		if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
//
//			String clientId = getClientIdentifier(httpRequest); // Get user identifier
//			long currentTime = System.currentTimeMillis();
//
//			// Check if the user is blocked
//			if (isBlocked(clientId, currentTime)) {
//				httpResponse.setStatus(SC_TOO_MANY_REQUESTS);
//				httpResponse.getWriter()
//						.write("You have been temporarily blocked due to too many requests. Try again later.");
//				return;
//			}
//
//			// Throttle logic
//			long lastRequestTime = clientLastRequestTime.getOrDefault(clientId, 0L);
//			if (currentTime - lastRequestTime < THROTTLE_INTERVAL) {
//				// If too many requests within the interval, block the user
//				blockedClients.put(clientId, currentTime);
//				httpResponse.setStatus(SC_TOO_MANY_REQUESTS);
//				httpResponse.getWriter().write("Too many requests. You have been temporarily blocked.");
//				return;
//			}
//
//			// Update last request time
//			clientLastRequestTime.put(clientId, currentTime);
//		}
//
//		// Allow request to proceed
//		chain.doFilter(request, response);
//	}
//
//	private String getClientIdentifier(HttpServletRequest request) {
//		// Example: Use IP address as identifier
//		return request.getRemoteAddr();
//	}
//
//	private boolean isBlocked(String clientId, long currentTime) {
//		Long blockTime = blockedClients.get(clientId);
//		if (blockTime == null) {
//			return false; // Not blocked
//		}
//		// Unblock if block duration has expired
//		if (currentTime - blockTime > BLOCK_DURATION) {
//			blockedClients.remove(clientId);
//			return false;
//		}
//		return true; // Still blocked
//	}
//
//	@Override
//	public void init(FilterConfig filterConfig) {
//	}
//
//	@Override
//	public void destroy() {
//	}
//}
