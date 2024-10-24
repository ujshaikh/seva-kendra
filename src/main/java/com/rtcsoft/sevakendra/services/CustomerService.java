//package com.rtcsoft.sevakendra.services;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.rtcsoft.sevakendra.controllers.UserController;
//import com.rtcsoft.sevakendra.dtos.CustomerDTO;
//import com.rtcsoft.sevakendra.entities.Customer;
//import com.rtcsoft.sevakendra.exceptions.CustomerException;
//import com.rtcsoft.sevakendra.repositories.CustomerRepository;
//
//import jakarta.transaction.Transactional;
//
//@Service
//@Transactional
//public class CustomerService {
//
//	@Autowired
//	private CustomerRepository customerRepository;
//
//	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
//
//	public ResponseEntity<Customer> create(CustomerDTO input) throws CustomerException {
//		try {
//			String fname = input.getFirstName();
//			String mname = input.getMiddleName();
//			String lname = input.getLastName();
//			String fullName = fname + " " + mname + " " + lname;
//
//			Optional<Customer> customerExist = customerRepository.findByFullName(fname, mname, lname);
//			if (customerExist.isPresent()) {
//				throw new CustomerException("Failed: Customer already exists with this name " + fullName);
//			}
//
//			Customer newCustomer = new Customer();
//			newCustomer.setFirstName(fname);
//			newCustomer.setMiddleName(mname);
//			newCustomer.setLastName(lname);
//			newCustomer.setPhoneNumber(input.getPhoneNumber());
//			newCustomer.setAddress(input.getAddress());
//			newCustomer.setPlace(input.getPlace());
//			newCustomer.setAge(input.getAge());
//			newCustomer.setCast(input.getCast());
//			newCustomer.setOccupation(input.getOccupation());
//			newCustomer.setAadharNumber(input.getAadharNumber());
//			newCustomer.setImage(input.getImage());
//			newCustomer.setUserId(input.getUserId());
//
//			// save() is an in built method given by JpaRepository
//			customerRepository.save(newCustomer);
//
//			return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
//
//		} catch (CustomerException e) {
//			throw new CustomerException(e.getMessage());
//		}
//	}
//
//	public List<Customer> getAllUsers() {
//		List<Customer> customers = new ArrayList<>();
//
//		customerRepository.findAll().forEach(customers::add);
//
//		return customers;
//	}
//
//	public Optional<Customer> findById(long id) {
//		return customerRepository.findById(id);
//	}
//
//	public ResponseEntity<Customer> update(CustomerDTO input, long id) throws CustomerException {
//		try {
//			String fname = input.getFirstName();
//			String mname = input.getMiddleName();
//			String lname = input.getLastName();
//			String fullName = fname + " " + mname + " " + lname;
//
//			Optional<Customer> customerExist = Optional.ofNullable(customerRepository.findById(id)
//					.orElseThrow(() -> new CustomerException("Customer not found with id " + id)));
//
//			if (customerExist.isPresent()) {
//				throw new CustomerException("Failed: Customer already exists with this name " + fullName);
//			}
//
//			Customer newCustomer = new Customer();
//			newCustomer.setFirstName(fname);
//			newCustomer.setMiddleName(mname);
//			newCustomer.setLastName(lname);
//			newCustomer.setPhoneNumber(input.getPhoneNumber());
//			newCustomer.setAddress(input.getAddress());
//			newCustomer.setPlace(input.getPlace());
//			newCustomer.setAge(input.getAge());
//			newCustomer.setCast(input.getCast());
//			newCustomer.setOccupation(input.getOccupation());
//			newCustomer.setAadharNumber(input.getAadharNumber());
//			newCustomer.setImage(input.getImage());
//			newCustomer.setUserId(input.getUserId());
//			return ResponseEntity.status(HttpStatus.OK).body(newCustomer);
//		} catch (Exception e) {
//			logger.error("Failed to update customer: " + e.getMessage());
//			throw new CustomerException(e.getMessage());
//		}
//	}
//
//	public ResponseEntity<Customer> delete(long id) throws CustomerException {
//		try {
//			Customer existingCustomer = customerRepository.findById(id)
//					.orElseThrow(() -> new CustomerException("Customer not found with id " + id));
//			customerRepository.deleteById(id);
//
//			return ResponseEntity.status(HttpStatus.OK).body(existingCustomer);
//		} catch (Exception e) {
//			logger.error("Failed to update customer: " + e.getMessage());
//			throw new CustomerException(e.getMessage());
//		}
//	}
//}

package com.rtcsoft.sevakendra.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rtcsoft.sevakendra.configs.StorageProperties;
import com.rtcsoft.sevakendra.controllers.UserController;
import com.rtcsoft.sevakendra.dtos.CustomerDTO;
import com.rtcsoft.sevakendra.entities.Customer;
import com.rtcsoft.sevakendra.exceptions.CustomerException;
import com.rtcsoft.sevakendra.repositories.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private static String DEST_PATH = "src/main/resources/output/";

	private final Path rootLocation;

	@Autowired
	public CustomerService(StorageProperties properties) {

		if (properties.getLocation().trim().length() == 0) {
			throw new RuntimeException("File upload location can not be Empty.");
		}

		this.rootLocation = Paths.get(properties.getLocation());
	}

	// Helper method to map CustomerDTO to Customer
	private Customer mapDtoToCustomer(CustomerDTO input) {
		Customer customer = new Customer();
		// Use existing value from database if input is null or empty
		customer.setFirstName(input.getFirstName() != null ? input.getFirstName() : customer.getFirstName());
		customer.setMiddleName(input.getMiddleName() != null ? input.getMiddleName() : customer.getMiddleName());
		customer.setLastName(input.getLastName() != null ? input.getLastName() : customer.getLastName());
		customer.setPhoneNumber(input.getPhoneNumber() != null ? input.getPhoneNumber() : customer.getPhoneNumber());
		customer.setAddress(input.getAddress() != null ? input.getAddress() : customer.getAddress());
		customer.setPlace(input.getPlace() != null ? input.getPlace() : customer.getPlace());
		customer.setAge(input.getAge() != null ? input.getAge() : customer.getAge());
		customer.setCast(input.getCast() != null ? input.getCast() : customer.getCast());
		customer.setOccupation(input.getOccupation() != null ? input.getOccupation() : customer.getOccupation());
		customer.setAadharNumber(
				input.getAadharNumber() != null ? input.getAadharNumber() : customer.getAadharNumber());
		customer.setImage(input.getImage() != null ? input.getImage() : customer.getImage());
		customer.setUserId(input.getUserId() != 0 ? input.getUserId() : customer.getUserId());
		return customer;
	}

	public ResponseEntity<Customer> create(CustomerDTO input, MultipartFile file)
			throws CustomerException, IllegalStateException, IOException {
		validateExistingEntry(input);

		// Upload image of customer and store its path in db
		Path newImageFile = store(file, input);

		Customer newCustomer = mapDtoToCustomer(input);
		newCustomer.setImage(newImageFile.toString());
		customerRepository.save(newCustomer);

		return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
	}

	public ResponseEntity<Customer> update(CustomerDTO input, MultipartFile file, long id) throws CustomerException {
		customerRepository.findById(id).orElseThrow(() -> new CustomerException("Customer not found with id " + id));

		validateExistingEntry(input);

		// Upload image of customer and store its path in db
		Path newImageFile = store(file, input);

		Customer updatedCustomer = mapDtoToCustomer(input);
		updatedCustomer.setId(id); // Preserve the existing ID
		updatedCustomer.setImage(newImageFile.toString());
		customerRepository.save(updatedCustomer);

		return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
	}

	public List<Customer> getAllUsers() {
		return customerRepository.findAll();
	}

	public Optional<Customer> findById(long id) {
		return customerRepository.findById(id);
	}

	public ResponseEntity<Customer> delete(long id) throws CustomerException {
		Customer existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerException("Customer not found with id " + id));

		customerRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(existingCustomer);
	}

	private void validateExistingEntry(CustomerDTO input) throws CustomerException {
		String fname = Optional.ofNullable(input.getFirstName()).orElse("").trim();
		String mname = Optional.ofNullable(input.getMiddleName()).orElse("").trim();
		String lname = Optional.ofNullable(input.getLastName()).orElse("").trim();

		// Construct full name by joining non-empty parts
		String fullName = Stream.of(fname, mname, lname).filter(part -> part != null && !part.isEmpty())
				.collect(Collectors.joining(" "));

		List<Customer> existingCustomers = customerRepository.findByFullName(fname, mname, lname);

		if (!existingCustomers.isEmpty()) {
			throw new CustomerException("Failed: Customer already exists with this name " + fullName);
		}
	}

	public Path store(MultipartFile file, CustomerDTO input) {
		try {
			if (file.isEmpty()) {
				throw new RuntimeException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize()
					.toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new RuntimeException("Cannot store file outside current directory.");
			}

			try (InputStream inputStream = file.getInputStream()) {
				String fname = input.getFirstName().toLowerCase();
				String lname = input.getLastName().toLowerCase();

				Path newDestinationFile = addPrefixToFile(destinationFile, fname, lname);
				Files.copy(inputStream, newDestinationFile, StandardCopyOption.REPLACE_EXISTING);
				return newDestinationFile;
			}

		} catch (IOException e) {
			throw new RuntimeException("Failed to store file.", e);
		}
	}

	public static Path addPrefixToFile(Path originalFilePath, String firstName, String lastName) throws IOException {
		// Extract the filename and its extension
		System.out.println("OG File Path");
		System.out.println(originalFilePath);
		String fileName = originalFilePath.getFileName().toString();
		String fileExtension = "";

		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex != -1) {
			fileExtension = fileName.substring(lastDotIndex); // Get the extension (e.g., .jpg)
			fileName = fileName.substring(0, lastDotIndex); // Get the base name (e.g., myself)
		}

		// Create the new file name with the prefix
		String newFileName = firstName + "-" + lastName + "-" + fileName + fileExtension; // e.g., John-Doe-myself.jpg

		// Create the new path
		Path newFilePath = originalFilePath.getParent().resolve(newFileName);

		return newFilePath;
	}
}
