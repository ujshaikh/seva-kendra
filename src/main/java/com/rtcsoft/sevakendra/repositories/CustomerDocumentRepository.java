package com.rtcsoft.sevakendra.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rtcsoft.sevakendra.entities.CustomerDocument;

@Repository
public interface CustomerDocumentRepository extends CrudRepository<CustomerDocument, Long> {
	Optional<CustomerDocument> findByDocName(String docName);

//	Optional<CustomerDocument> findByUserId(String userId);

	Optional<CustomerDocument> findById(long id);

	@Override
	List<CustomerDocument> findAll();

	void deleteById(long id);

}