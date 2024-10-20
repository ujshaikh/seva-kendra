package com.rtcsoft.setu_kendra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rtcsoft.setu_kendra.entities.User;

@Repository
public interface ResetPasswordRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User findByToken(String token);
}