package com.rtcsoft.sevakendra.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rtcsoft.sevakendra.dtos.LoginUserDTO;
import com.rtcsoft.sevakendra.dtos.RegisterUserDTO;
import com.rtcsoft.sevakendra.entities.User;
import com.rtcsoft.sevakendra.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User signup(RegisterUserDTO input) {

		User user = new User();

		user.setFullName(input.getFullName());
		user.setEmail(input.getEmail());
		user.setMobileNumber(input.getMobileNumber());
		user.setPassword(passwordEncoder.encode(input.getPassword()));

		return userRepository.save(user);
	}

	public User authenticate(LoginUserDTO input) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}

	public void logoutUser(HttpSession session) {
		if (session != null) {
			session.invalidate(); // Invalidate the session
		}

		// Clear the authentication context
		SecurityContextHolder.clearContext();

		// Additional custom logic (e.g., logging or auditing) can go here
		System.out.println("User has been logged out and session invalidated.");
	}
}