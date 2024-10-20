package com.rtcsoft.sevakendra.dtos;

public class RegisterUserDTO {

	private String email;

	private String password;

	private String fullName;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public CharSequence getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
}