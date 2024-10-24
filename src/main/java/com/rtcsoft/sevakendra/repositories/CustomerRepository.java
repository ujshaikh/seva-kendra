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

}