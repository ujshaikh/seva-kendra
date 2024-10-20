package com.rtcsoft.setu_kendra.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseDTO<T> {
	private String status;
	private T response;

	public ApiResponseDTO(String status, T response) {
		super();
		this.status = status;
		this.response = response;
	}
}
