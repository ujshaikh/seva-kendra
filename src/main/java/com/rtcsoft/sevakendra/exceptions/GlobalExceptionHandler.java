package com.rtcsoft.sevakendra.exceptions;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rtcsoft.sevakendra.responses.ApiResponse;
import com.rtcsoft.sevakendra.utils.ResponseUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleSecurityException(Exception exception) {
		ProblemDetail errorDetail = null;

		// TODO send this stack trace to an observability tool
		exception.printStackTrace();

		if (exception instanceof UsernameNotFoundException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "This account not approved, contact to admin");

			return errorDetail;
		}

		if (exception instanceof BadCredentialsException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
			errorDetail.setProperty("description", "The username or password is incorrect");

			return errorDetail;
		}

		if (exception instanceof AccountStatusException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "The account is locked");
		}

		if (exception instanceof AccessDeniedException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "You are not authorized to access this resource");
		}

		if (exception instanceof SignatureException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "The JWT signature is invalid");
		}

		if (exception instanceof ExpiredJwtException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "The JWT token has expired");
		}

		if (errorDetail == null) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
			errorDetail.setProperty("description", "Unknown internal server error.");
		}

		return errorDetail;
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(
			DataIntegrityViolationException ex) {
		// Default message for duplicate entry
		String message = "Duplicate entry. This user already exists.";

		// Check the root cause message for database constraint violations
		if (ex.getCause() != null && ex.getCause().getMessage() != null) {
			String detailedMessage = ex.getCause().getMessage();

			// Customize message based on the detailed exception message content
			if (detailedMessage.contains("duplicate") || detailedMessage.contains("constraint")) {
				message = "Duplicate user detected: This username or email already exists.";
			}
		}

		return ResponseUtil.errorResponse(List.of(ex.getMessage()), message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
