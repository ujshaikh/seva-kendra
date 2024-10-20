package com.rtcsoft.sevakendra.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rtcsoft.sevakendra.entities.User;
import com.rtcsoft.sevakendra.repositories.UserRepository;

@Service
public class ResetPasswordService {
	private static final long EXPIRE_TOKEN = 30;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private final PasswordEncoder passwordEncoder;

	public ResetPasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public String forgotPassword(String email) {
		Optional<User> userOptional = userRepository.findByEmail(email);

		if (userOptional.isEmpty()) {
			return "Invalid email id.";
		}

		User user = userOptional.get();
		user.setToken(generateToken());
		user.setTokenCreationDate(LocalDateTime.now());

		user = userRepository.save(user);
		return user.getToken();
	}

	public String resetPassword(String token, String password) {
		System.out.println("Password" + password);
		Optional<Object> userOptional = Optional.ofNullable(userRepository.findByToken(token));

		if (!userOptional.isPresent()) {
			return "Invalid token";
		}
		LocalDateTime tokenCreationDate = ((User) userOptional.get()).getTokenCreationDate();

		if (isTokenExpired(tokenCreationDate)) {
			return "Token expired.";

		}

		User user = (User) userOptional.get();

		user.setPassword(passwordEncoder.encode(password));
		user.setToken(null);
		user.setTokenCreationDate(null);

		userRepository.save(user);

		return "Your password successfully updated.";
	}

	private String generateToken() {
		StringBuilder token = new StringBuilder();

		return token.append(UUID.randomUUID().toString()).append(UUID.randomUUID().toString()).toString();
	}

	private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);

		return diff.toMinutes() >= EXPIRE_TOKEN;
	}

}