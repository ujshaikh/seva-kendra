package com.rtcsoft.sevakendra.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO {
	@Email(message = "Email is not in valid format!")
	@NotBlank(message = "Email is required!")
	private String email;

	@NotBlank(message = "Password required!")
	@Size(min = 4, max = 10, message = "Password must have 4 to 10 characters!")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Object getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
}
