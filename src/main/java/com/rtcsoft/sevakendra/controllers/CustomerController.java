package com.rtcsoft.sevakendra.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rtcsoft.sevakendra.dtos.CustomerDTO;
import com.rtcsoft.sevakendra.entities.Customer;
import com.rtcsoft.sevakendra.exceptions.ApiException;
import com.rtcsoft.sevakendra.services.CustomerService;
import com.rtcsoft.sevakendra.services.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;

@RequestMapping("/customer")
@RestController
public class CustomerController {
	@Autowired
	private final JwtService jwtService;

	@Autowired
	private final CustomerService customerService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	public CustomerController(CustomerService customerService, JwtService jwtService) {
		this.jwtService = jwtService;
		this.customerService = customerService;
	}

	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<Customer> create(@ModelAttribute CustomerDTO customer,
			@RequestPart("file") MultipartFile file) throws ApiException, IllegalStateException, IOException {
		return customerService.create(customer, file);
	}

	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<Customer> update(@ModelAttribute CustomerDTO customer,
			@RequestPart(value = "file", required = false) MultipartFile file, @PathVariable long id)
			throws ApiException, IOException {
		return customerService.update(customer, file, id);
	}

	@GetMapping("/list")
	public ResponseEntity<List<Customer>> list(@NonNull HttpServletRequest request) throws ApiException {
		return customerService.getAllUsers(request);
	}

	@GetMapping("{id}")
	public ResponseEntity<Optional<Customer>> getById(@PathVariable Long id) throws ApiException {
		return customerService.findById(id);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Customer> delete(@PathVariable Long id) throws ApiException {
		return customerService.delete(id);
	}
}