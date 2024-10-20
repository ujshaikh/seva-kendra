package com.rtcsoft.sevakendra.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rtcsoft.sevakendra.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByEmail(String email);

	Object findByToken(String token);
}