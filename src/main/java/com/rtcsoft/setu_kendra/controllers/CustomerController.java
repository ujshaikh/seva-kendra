package com.rtcsoft.setu_kendra.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rtcsoft.setu_kendra.dtos.CustomerDTO;
import com.rtcsoft.setu_kendra.entities.Customer;
import com.rtcsoft.setu_kendra.exceptions.CustomerException;
import com.rtcsoft.setu_kendra.services.CustomerService;
import com.rtcsoft.setu_kendra.services.JwtService;

import jakarta.validation.Valid;

@RequestMapping("/customer")
@RestController
public class CustomerController {
	private final JwtService jwtService;

	@Autowired
	private final CustomerService customerService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	public CustomerController(JwtService jwtService, CustomerService authService) {
		this.jwtService = jwtService;
		this.customerService = authService;
	}

	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> create(@Valid @RequestBody CustomerDTO customer) throws CustomerException {
		System.out.println(customer.getName());
		return customerService.create(customer);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Customer> update(@Valid @RequestBody CustomerDTO customer, @PathVariable long id)
			throws CustomerException {
		return customerService.update(customer, id);
	}

	@GetMapping("/list")
	public List<Customer> customerList() throws CustomerException {
		return customerService.getAllUsers();
	}

	@GetMapping("{id}")
	public Optional<Customer> getCustomer(@PathVariable Long id) throws CustomerException {
		System.out.println("ID " + id);
		return customerService.findById(id);
	}
}