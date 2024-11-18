package com.rtcsoft.sevakendra.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rtcsoft.sevakendra.entities.CustomerDocument;

@Repository
public interface CustomerDocumentRepository extends CrudRepository<CustomerDocument, Long> {
	Optional<CustomerDocument> findByDocName(String docName);

	List<CustomerDocument> findByUserId(long userId);

	@Query("SELECT c FROM Customer c WHERE c.id = ?1 AND c.userId = ?2")
	Optional<CustomerDocument> findWithUserId(long id, long userId);

	@Override
	List<CustomerDocument> findAll();

	List<CustomerDocument> findAllByUserId(Long userId);

	void deleteById(long id);

	@Query("SELECT COUNT(c) FROM CustomerDocument c WHERE c.userId = ?1")
	long findCountById(Long userId);

	@Query("SELECT c FROM CustomerDocument c WHERE c.userId = ?1 AND c.customerId = ?2")
	List<CustomerDocument> findAllByCustomerId(long userId, int customerId);
}