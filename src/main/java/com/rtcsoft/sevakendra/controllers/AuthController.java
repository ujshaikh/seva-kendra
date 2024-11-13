package com.rtcsoft.sevakendra.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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
import com.rtcsoft.sevakendra.responses.ApiResponse;
import com.rtcsoft.sevakendra.services.AuthService;
import com.rtcsoft.sevakendra.services.JwtService;
import com.rtcsoft.sevakendra.services.ResetPasswordService;
import com.rtcsoft.sevakendra.services.TokenBlacklistService;
import com.rtcsoft.sevakendra.utils.ResponseUtil;

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
	public ResponseEntity<ApiResponse<User>> register(@RequestBody RegisterUserDTO registerUserDto) {
		try {
			User registeredUser = authService.signup(registerUserDto);

			return ResponseUtil.successResponse(registeredUser, "Successfuly submitted");
		} catch (DataIntegrityViolationException e) {
			throw e;
		}
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<HashMap<String, Object>>> authenticate(@RequestBody LoginUserDTO loginUserDto) {
		try {

			User authenticatedUser = authService.authenticate(loginUserDto);
			// Store the user ID in session
			HttpSession session = request.getSession();
			Long userId = authenticatedUser.getId();
			System.out.println("Setting Session " + userId);
			session.setAttribute("userId", userId);
			session.setAttribute("userName", authenticatedUser.getEmail());

			String jwtToken = jwtService.generateToken(authenticatedUser);

			HashMap<String, Object> data = new HashMap<>();
			data.put("token", jwtToken);
			data.put("expiresIn", jwtService.getExpirationTime());
			data.put("id", userId);
			data.put("fullName", authenticatedUser.getFullName());
			data.put("email", authenticatedUser.getEmail());

			return ResponseUtil.successResponse(data, null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseUtil.errorResponse("", "Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
		}

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
	public ResponseEntity<ApiResponse<HashMap<String, String>>> forgotPassword(@RequestBody LoginUserDTO loginUserDto,
			HttpServletRequest request) throws URISyntaxException {
		String token = resetPasswordService.forgotPassword(loginUserDto.getEmail());

		if (!token.startsWith("Invalid")) {
			HashMap<String, String> data = new HashMap<>();
			data.put("token", token);
			return ResponseUtil.successResponse(data, null);
		}
		return ResponseUtil.errorResponse("", "Failed to find data in record", HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestBody LoginUserDTO loginUserDto) {
		String newPassword = loginUserDto.getPassword().toString();
		return resetPasswordService.resetPassword(token, newPassword);
	}
}