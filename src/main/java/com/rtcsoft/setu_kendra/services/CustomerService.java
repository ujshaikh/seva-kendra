package com.rtcsoft.setu_kendra.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rtcsoft.setu_kendra.controllers.UserController;
import com.rtcsoft.setu_kendra.dtos.CustomerDTO;
import com.rtcsoft.setu_kendra.entities.Customer;
import com.rtcsoft.setu_kendra.exceptions.CustomerException;
import com.rtcsoft.setu_kendra.repositories.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	public ResponseEntity<Customer> create(CustomerDTO input) throws CustomerException {
		System.out.println("Input " + input.toString());
		try {
			Optional<Customer> customerExist = customerRepository.findByName(input.getName());
			if (customerExist.isPresent()) {
				throw new CustomerException("Failed: Customer already exists with this name " + input.getName());
			}

			Customer newCustomer = new Customer();
			newCustomer.setName(input.getName());
			newCustomer.setPhoneNumber(input.getPhoneNumber());
			newCustomer.setAddress(input.getAddress());
//			newCustomer.setActive(input.get);

			// save() is an in built method given by JpaRepository
			customerRepository.save(newCustomer);

			return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);

		} catch (CustomerException e) {
			throw new CustomerException(e.getMessage());
		}
	}

	public List<Customer> getAllUsers() {
		List<Customer> customers = new ArrayList<>();

		customerRepository.findAll().forEach(customers::add);

		return customers;
	}

	public Optional<Customer> findById(long id) {
		return customerRepository.findById(id);
	}

	public ResponseEntity<Customer> update(CustomerDTO input, long id) throws CustomerException {
		try {
			Optional<Customer> customerExist = Optional.ofNullable(customerRepository.findById(id)
					.orElseThrow(() -> new CustomerException("Customer not found with id " + id)));

			if (customerExist.isPresent()) {
				throw new CustomerException("Failed: Customer already exists with this name " + input.getName());
			}

			Customer newCustomer = new Customer();
			newCustomer.setName(input.getName());
			newCustomer.setPhoneNumber(input.getPhoneNumber());
			newCustomer.setAddress(input.getAddress());
			newCustomer.setActive(input.isActive());
			return ResponseEntity.status(HttpStatus.OK).body(newCustomer);
		} catch (Exception e) {
			logger.error("Failed to update customer: " + e.getMessage());
			throw new CustomerException(e.getMessage());
		}
	}

//	@Override
//	public ResponseEntity<ApiResponseDto<?>> deleteUser(int id)
//			throws UserServiceLogicException, UserNotFoundException {
//		// logic to delete user
//	}
}
