package com.rtcsoft.sevakendra.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rtcsoft.sevakendra.dtos.CustomerDTO;
import com.rtcsoft.sevakendra.entities.Customer;
import com.rtcsoft.sevakendra.exceptions.ApiException;
import com.rtcsoft.sevakendra.responses.ApiResponse;
import com.rtcsoft.sevakendra.services.CustomerService;
import com.rtcsoft.sevakendra.services.JwtService;
import com.rtcsoft.sevakendra.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.NonNull;

@RequestMapping("/customer")
@RestController
@Transactional
public class CustomerController {
	@Autowired
	private final JwtService jwtService;

	@Autowired
	private final CustomerService customerService;

	@Autowired
	private final ResourceLoader resourceLoader;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	public CustomerController(CustomerService customerService, JwtService jwtService, ResourceLoader resourceLoader) {
		this.jwtService = jwtService;
		this.customerService = customerService;
		this.resourceLoader = resourceLoader;
	}

	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<Customer> create(@NonNull HttpServletRequest request, @ModelAttribute CustomerDTO customer,
			@RequestParam(value = "file", required = false) MultipartFile file)
			throws ApiException, IllegalStateException, IOException {
		return customerService.create(request, customer, file);
	}

	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<Customer> update(@NonNull HttpServletRequest request, @ModelAttribute CustomerDTO customer,
			@RequestParam(value = "file", required = false) MultipartFile file, @PathVariable long id)
			throws ApiException, IOException {
		return customerService.update(request, customer, file, id);
	}

	@GetMapping("/list")
	public ResponseEntity<ApiResponse<List<Customer>>> list(@NonNull HttpServletRequest request) throws ApiException {
		try {
			List<Customer> customer = customerService.getAllCustomers(request);
			return ResponseUtil.successResponse(customer, "Fetched customer list");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseUtil.errorResponse("", "Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/list/recent")
	public ResponseEntity<ApiResponse<List<Customer>>> listRecent(
			@RequestParam(value = "limit", defaultValue = "5") Integer limit, @NonNull HttpServletRequest request)
			throws ApiException {
		try {
			List<Customer> customer = customerService.getRecentCustomers(request, limit);
			return ResponseUtil.successResponse(customer, "Fetched customer list");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseUtil.errorResponse("", "Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("{id}")
	public ResponseEntity<Optional<Customer>> getById(@PathVariable Long id) throws ApiException {
		return customerService.findById(id);
	}

	@GetMapping("/images/{filename}")
	public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
		Resource image = resourceLoader.getResource("classpath:static/uploads/" + filename);

		if (!image.exists()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");

		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Customer> delete(@PathVariable Long id) throws ApiException {
		return customerService.delete(id);
	}
}