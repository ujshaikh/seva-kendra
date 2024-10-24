package com.rtcsoft.sevakendra.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDTO {

	@NotBlank(message = "Customer first name is required!")
	@Size(min = 3, message = "Customer name must have atleast 3 characters!")
	@Size(max = 50, message = "Customer name can have have atmost 50 characters!")
	private String firstName;

	@Size(min = 3, message = "Customer middle name must have atleast 3 characters!")
	@Size(max = 50, message = "Customer middle name can have have atmost 50 characters!")
	private String middleName;

	@NotBlank(message = "Customer last name is required!")
	@Size(min = 3, message = "Customer last name must have atleast 3 characters!")
	@Size(max = 50, message = "Customer last name can have have atmost 50 characters!")
	private String lastName;

	@Size(min = 0, message = "Invalid age")
	@Size(max = 100, message = "Invalid")
	private String age;

	@Size(min = 3, max = 10)
	private String cast;

	@Size(min = 3, max = 10)
	private String occupation;

	@Size(min = 3, max = 20)
	private String place;

	@Size(min = 12, max = 12)
	private String aadharNumber;

	@Size(min = 10, max = 10, message = "Phone number must have 10 characters!")
	@Pattern(regexp = "^[0-9]*$", message = "Phone number must contain only digits")
	private String phoneNumber;

	private String address;

	private long userId;

	private Boolean isActive = true;

	private String image;
}
