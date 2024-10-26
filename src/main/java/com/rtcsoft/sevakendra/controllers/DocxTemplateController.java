package com.rtcsoft.sevakendra.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rtcsoft.sevakendra.dtos.CustomerDocumentDTO;
import com.rtcsoft.sevakendra.entities.CustomerDocument;
import com.rtcsoft.sevakendra.exceptions.ApiException;
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
	public DocxTemplateController(DocxTemplateService docService, JwtService jwtService) {
		this.jwtService = jwtService;
		this.docService = docService;
	}

	@PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerDocument> generate(@Valid @RequestBody CustomerDocumentDTO customerDocument)
			throws ApiException, IOException {

		return docService.createOrUpdate(customerDocument);
	}

	@GetMapping("/list")
	public ResponseEntity<List<CustomerDocument>> list() throws ApiException {
		return docService.getAllDocuments();
	}

	@GetMapping("{id}")
	public ResponseEntity<Optional<CustomerDocument>> getById(@PathVariable Long id) {
		return docService.findById(id);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<CustomerDocument> delete(@PathVariable Long id) throws ApiException {
		return docService.delete(id);
	}
}
