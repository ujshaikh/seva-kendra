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

	@NotBlank(message = "Customer name is required!")
	@Size(min = 3, message = "Customer name must have atleast 3 characters!")
	@Size(max = 50, message = "Customer name can have have atmost 50 characters!")
	private String name;

	@NotBlank(message = "Phone number is required!")
	@Size(min = 10, max = 10, message = "Phone number must have 10 characters!")
	@Pattern(regexp = "^[0-9]*$", message = "Phone number must contain only digits")
	private String phoneNumber;

	@Getter
	private String address;

	private boolean isActive;
}
