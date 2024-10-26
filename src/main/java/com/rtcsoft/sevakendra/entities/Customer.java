package com.rtcsoft.sevakendra.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull
	@Column
	private Long id;

	@NotBlank
	@Column(length = 50)
	private String firstName;

	@Column(length = 50)
	private String middleName;

	@NotBlank
	@Column(length = 50)
	private String lastName;

	@Column(length = 10)
	private String age;

	@Column(length = 20)
	private String cast;

	@Column(length = 20)
	private String occupation;

	@Column(length = 20)
	private String place;

	@Column(length = 20)
	private String aadharNumber;

	@Column(length = 20)
	private String phoneNumber;

	@Column(length = 255)
	private String address;

	@Column
	private Boolean isActive = true;

	@Column
	private String image;

	@NotNull
	@Column
	private Long userId;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column
	private LocalDateTime updatedAt;
}