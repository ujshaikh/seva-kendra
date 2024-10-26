package com.rtcsoft.sevakendra.configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.rtcsoft.sevakendra.services.JwtService;
import com.rtcsoft.sevakendra.services.SharedService;
import com.rtcsoft.sevakendra.services.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	private final HandlerExceptionResolver handlerExceptionResolver;

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Autowired
	private final SharedService sharedService;
	@Autowired
	HttpSession session;

	@Autowired
	private TokenBlacklistService tokenBlacklistService;

	public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService,
			HandlerExceptionResolver handlerExceptionResolver, SharedService sharedService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.handlerExceptionResolver = handlerExceptionResolver;
		this.sharedService = sharedService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String jwt = authHeader.substring(7);
			final String userEmail = jwtService.extractUsername(jwt);

			// Check if the token is blacklisted
			if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been blacklisted");
				return;
			}

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (userEmail != null && authentication == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

				if (jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);

					// Get userName form http session: Added on 26/10/2024
					if (!sharedService.checkAccessBySessionAtrribute(request)) {
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorised access!");
						return;
					}
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			handlerExceptionResolver.resolveException(request, response, null, exception);
		}
	}
}