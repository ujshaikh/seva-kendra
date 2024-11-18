package com.rtcsoft.sevakendra.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rtcsoft.sevakendra.dtos.DocxTemplateRequestBodyDTO;
import com.rtcsoft.sevakendra.dtos.DocxTemplateRequestBodyDTO.DocTemplate;
import com.rtcsoft.sevakendra.entities.CustomerDocument;
import com.rtcsoft.sevakendra.exceptions.ApiException;
import com.rtcsoft.sevakendra.responses.ApiResponse;
import com.rtcsoft.sevakendra.services.DocxTemplateService;
import com.rtcsoft.sevakendra.services.JwtService;

import jakarta.validation.Valid;

@RequestMapping("/customer-documents")
@RestController
public class DocxTemplateController {

	@Autowired
	private final DocxTemplateService docService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private final ResourceLoader resourceLoader;

	@Autowired
	public DocxTemplateController(DocxTemplateService docService, JwtService jwtService,
			ResourceLoader resourceLoader) {
		this.jwtService = jwtService;
		this.docService = docService;
		this.resourceLoader = resourceLoader;
	}

	@PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DocTemplate>> generate(@Valid @RequestBody DocxTemplateRequestBodyDTO input)
			throws ApiException, IOException {

		return docService.createOrUpdate(input);
	}

	@GetMapping("/list")
	public ResponseEntity<ApiResponse<List<CustomerDocument>>> list(
			@RequestParam(value = "customerId", defaultValue = "-1") int customerId) throws ApiException {
		return docService.getAllDocuments(customerId);
	}

	@GetMapping("{id}")
	public ResponseEntity<Optional<CustomerDocument>> getById(@PathVariable Long id) {
		return docService.findById(id);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<CustomerDocument> delete(@PathVariable Long id) throws ApiException {
		return docService.delete(id);
	}

	@GetMapping("/generated/docs/{filename}")
	public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
		Resource file = resourceLoader.getResource("classpath:static/generated/docs/" + filename);

		if (!file.exists()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE,
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document");

		return new ResponseEntity<>(file, headers, HttpStatus.OK);
	}
}
