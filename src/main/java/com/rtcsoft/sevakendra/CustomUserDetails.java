package com.rtcsoft.sevakendra;

import org.springframework.security.core.userdetails.UserDetails;

import com.rtcsoft.sevakendra.entities.User;

public abstract class CustomUserDetails implements UserDetails {
	private Long userId;

	public CustomUserDetails(User user) {
		this.userId = user.getId();
	}

	public Long getId() {
		return userId;
	}
}
