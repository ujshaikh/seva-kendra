package com.rtcsoft.sevakendra.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rtcsoft.sevakendra.entities.User;
import com.rtcsoft.sevakendra.services.UserService;

@RequestMapping("/users")
@RestController
public class UserController {
	private final UserService userService;

	// Get the SLF4J logger interface, default Logback, a SLF4J implementation
	// private static final Logger logger =
	// LoggerFactory.getLogger(UserController.class);

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<User> authenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User currentUser = (User) authentication.getPrincipal();

		return ResponseEntity.ok(currentUser);
	}

	@GetMapping("/")
	public ResponseEntity<List<User>> allUsers() {
		List<User> users = userService.allUsers();

		return ResponseEntity.ok(users);
	}
}