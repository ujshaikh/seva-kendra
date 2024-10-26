package com.rtcsoft.sevakendra.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rtcsoft.sevakendra.dtos.LoginUserDTO;
import com.rtcsoft.sevakendra.dtos.RegisterUserDTO;
import com.rtcsoft.sevakendra.entities.User;
import com.rtcsoft.sevakendra.responses.LoginResponse;
import com.rtcsoft.sevakendra.services.AuthService;
import com.rtcsoft.sevakendra.services.JwtService;
import com.rtcsoft.sevakendra.services.ResetPasswordService;
import com.rtcsoft.sevakendra.services.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/auth")
@RestController
public class AuthController {
	private final JwtService jwtService;

	@Autowired
	private final AuthService authService;
	@Autowired
	private ResetPasswordService resetPasswordService;
	@Autowired
	HttpServletRequest request;

	@Autowired
	private TokenBlacklistService tokenBlacklistService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	public AuthController(JwtService jwtService, AuthService authService) {
		this.jwtService = jwtService;
		this.authService = authService;
	}

	@GetMapping("/test")
	public ResponseEntity<User> test(HttpServletRequest request) throws URISyntaxException {
		String urlString = request.getRequestURL().toString();
		URI uri = new URI(urlString);
		System.out.println("homeURL" + uri.getHost() + uri.getPort() + "=>" + uri.toString());

		return ResponseEntity.ok(null);
	}

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDto) {
		User registeredUser = authService.signup(registerUserDto);

		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDto) {
		User authenticatedUser = authService.authenticate(loginUserDto);
		// Store the user ID in session
		HttpSession session = request.getSession();
		Long userId = authenticatedUser.getId();
		System.out.println("Setting Session " + userId);
		session.setAttribute("userId", userId);
		session.setAttribute("userName", authenticatedUser.getEmail());

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponse loginResponse = new LoginResponse().setToken(jwtToken)
				.setExpiresIn(jwtService.getExpirationTime()).setUserId(userId);

		return ResponseEntity.ok(loginResponse);
	}

	@PostMapping("/logout")
	public String logout(@RequestHeader("Authorization") String token, HttpSession session) {
		if (token != null && token.startsWith("Bearer ")) {
			String jwtToken = token.substring(7); // Remove "Bearer " prefix
			tokenBlacklistService.addTokenToBlacklist(jwtToken);
		}

		authService.logoutUser(session);
		return "User logged out successfully";
	}

	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestBody LoginUserDTO loginUserDto, HttpServletRequest request)
			throws URISyntaxException {
		String response = resetPasswordService.forgotPassword(loginUserDto.getEmail());

		if (!response.startsWith("Invalid")) {
			String urlString = request.getRequestURL().toString();
			URI uri = new URI(urlString);
			String domainUri = uri.getHost() + ":" + uri.getPort();
			response = "http://" + domainUri + "/auth/reset-password?token=" + response;
		}
		return response;
	}

	@PutMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestBody LoginUserDTO loginUserDto) {
		String newPassword = loginUserDto.getPassword().toString();
		return resetPasswordService.resetPassword(token, newPassword);
	}
}