package com.rtcsoft.sevakendra.utils;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rtcsoft.sevakendra.responses.ApiResponse;

public class ResponseUtil {

	public static <T> ResponseEntity<ApiResponse<T>> successResponse(T data, String message) {
		ApiResponse<T> response = new ApiResponse<>(data, message, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public static <T> ResponseEntity<ApiResponse<T>> errorResponse(List<String> errors, String message,
			HttpStatus status) {
		ApiResponse<T> response = new ApiResponse<>(null, message, errors);
		return new ResponseEntity<>(response, status);
	}

	public static <T> ResponseEntity<ApiResponse<T>> errorResponse(String error, String message, HttpStatus status) {
		return errorResponse(Collections.singletonList(error), message, status);
	}

	public static <T> ResponseEntity<ApiResponse<T>> errorResponse(boolean error, String message, HttpStatus status) {
		return errorResponse(error, message, status);
	}
}
