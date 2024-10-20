package com.rtcsoft.setu_kendra.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rtcsoft.setu_kendra.dtos.LoginUserDTO;
import com.rtcsoft.setu_kendra.dtos.RegisterUserDTO;
import com.rtcsoft.setu_kendra.entities.User;
import com.rtcsoft.setu_kendra.responses.LoginResponse;
import com.rtcsoft.setu_kendra.services.AuthService;
import com.rtcsoft.setu_kendra.services.JwtService;
import com.rtcsoft.setu_kendra.services.ResetPasswordService;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/auth")
@RestController
public class AuthController {
	private final JwtService jwtService;

	@Autowired
	private final AuthService authService;
	@Autowired
	private ResetPasswordService resetPasswordService;

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
		System.out.println("Test Login");
		User authenticatedUser = authService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponse loginResponse = new LoginResponse().setToken(jwtToken)
				.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}

	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestBody LoginUserDTO loginUserDto, HttpServletRequest request)
			throws URISyntaxException {
//		logger.info("Forgot Password ");
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