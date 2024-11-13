package com.rtcsoft.sevakendra.responses;

import java.util.List;

public class ApiResponse<T> {
	private T data;
	private String message;
	private List<String> errors;

	public ApiResponse() {
	}

	public ApiResponse(T data, String message, List<String> errors) {
		this.data = data;
		this.message = message;
		this.errors = errors;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
