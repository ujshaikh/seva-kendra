package com.rtcsoft.sevakendra.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterUserDTO {
	@Email(message = "Email is not in valid format!")
	@NotBlank(message = "Email is required!")
	private String email;

	@NotBlank(message = "Password required!")
	@Size(min = 4, max = 10, message = "Password must have 4 to 10 characters!")
	private String password;

	private String fullName;

	@NotBlank(message = "Mobile number required!")
	@Size(min = 10, max = 10, message = "Invalid mobile number")
	private String mobileNumber;
}