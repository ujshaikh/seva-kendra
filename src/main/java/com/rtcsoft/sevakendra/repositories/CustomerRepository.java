package com.rtcsoft.sevakendra.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rtcsoft.sevakendra.entities.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	@Query("SELECT c FROM Customer c WHERE c.firstName = ?1 AND c.middleName = ?2 AND c.lastName = ?3")
	ArrayList<Customer> findByFullName(String firstName, String middleName, String lastName);

	Optional<Customer> findById(long id);

	@Override
	List<Customer> findAll();

	void deleteById(long id);

	@Query("SELECT c FROM Customer c WHERE c.firstName = ?1 AND c.middleName = ?2 AND c.lastName = ?3 AND c.id != id")
	ArrayList<Customer> findByFullNameExcludingMe(long id, String fname, String mname, String lname);

	// @Query("SELECT c FROM Customer c WHERE c.userId = ?1")
	ArrayList<Customer> findAllByUserId(Long userId);

	@Query("SELECT c FROM Customer c WHERE c.userId = ?1 ORDER BY c.updatedAt DESC LIMIT ?2")
	ArrayList<Customer> findRecentByUserId(Long userId, int limit);

	@Query("SELECT COUNT(c) FROM Customer c WHERE c.userId = ?1")
	long findCountById(Long userId);

//	List<Customer> findAllById(long userId);

}