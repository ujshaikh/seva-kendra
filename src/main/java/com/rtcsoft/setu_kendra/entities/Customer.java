package com.rtcsoft.setu_kendra.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	@Column(nullable = false)
	private Integer id;

	@NotBlank
	@Size(min = 3, max = 50)
	private String name;

	@Size(min = 10, max = 10)
	private String phoneNumber;

	@Column(length = 255)
	private String address;

	@Column(name = "is_active", length = 255)
	private boolean isActive = true;

	@CreationTimestamp
	@Column(updatable = false, name = "created_at")
	private Date createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Date updatedAt;

//	public Integer getId() {
//		return id;
//	}
//
//	public Customer setId(Integer id) {
//		this.id = id;
//		return this;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public Customer setName(String name) {
//		this.name = name;
//		return this;
//	}
//
//	public Customer setPhone(String phoneNumber) {
//		this.phoneNumber = phoneNumber;
//		return this;
//	}
//
//	public Date getCreatedAt() {
//		return createdAt;
//	}
//
//	public Customer setCreatedAt(Date createdAt) {
//		this.createdAt = createdAt;
//		return this;
//	}
//
//	public Date getUpdatedAt() {
//		return updatedAt;
//	}
//
//	public Customer setUpdatedAt(Date updatedAt) {
//		this.updatedAt = updatedAt;
//		return this;
//	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", fullName='" + name + '\'' + ", isActive=" + isActive + ", address=" + address
				+ '}';
	}
}