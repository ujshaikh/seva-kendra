package com.rtcsoft.sevakendra.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rtcsoft.sevakendra.dtos.CustomerDocumentDTO;
import com.rtcsoft.sevakendra.entities.CustomerDocument;
import com.rtcsoft.sevakendra.exceptions.CustomerException;
import com.rtcsoft.sevakendra.services.DocxTemplateService;
import com.rtcsoft.sevakendra.services.JwtService;

import jakarta.validation.Valid;

@RequestMapping("/document")
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
			throws CustomerException, IOException {
		System.out.println(customerDocument.toString());

		return docService.create(customerDocument);
	}

}
