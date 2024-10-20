package com.rtcsoft.sevakendra.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rtcsoft.sevakendra.dtos.LoginUserDTO;
import com.rtcsoft.sevakendra.dtos.RegisterUserDTO;
import com.rtcsoft.sevakendra.entities.User;
import com.rtcsoft.sevakendra.repositories.UserRepository;

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
		User user = new User().setFullName(input.getFullName()).setEmail(input.getEmail())
				.setPassword(passwordEncoder.encode(input.getPassword()));

		return userRepository.save(user);
	}

	public User authenticate(LoginUserDTO input) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}
}