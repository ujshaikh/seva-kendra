package com.rtcsoft.sevakendra.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rtcsoft.sevakendra.entities.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	Optional<Customer> findByName(String name);

	Optional<Customer> findById(long id);

	@Override
	List<Customer> findAll();

	void deleteById(long id);

}