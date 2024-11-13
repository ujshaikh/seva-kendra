package com.rtcsoft.sevakendra.configs;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rtcsoft.sevakendra.controllers.UserController;
import com.rtcsoft.sevakendra.entities.User;
import com.rtcsoft.sevakendra.repositories.UserRepository;

@Configuration
public class ApplicationConfig {
	private final UserRepository userRepository;
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	public ApplicationConfig(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

//	@Bean
//	UserDetailsService userDetailsService() {
//		return username -> userRepository.findByEmail(username)
//				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
//	}

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			Optional<User> userOptional = userRepository.findByEmail(username);
			if (userOptional.isEmpty()) {
				throw new UsernameNotFoundException("User not found");
			}
			userOptional.ifPresent(user -> {
				if (!user.isAccountApproved()) {
					logger.error("User not approved");
					throw new UsernameNotFoundException("User not approved");
				}
			});
			return userOptional.get();
		};
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}
}
