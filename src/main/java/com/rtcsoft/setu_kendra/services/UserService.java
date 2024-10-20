package com.rtcsoft.setu_kendra.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rtcsoft.setu_kendra.entities.User;
import com.rtcsoft.setu_kendra.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> allUsers() {
		List<User> users = new ArrayList<>();

		userRepository.findAll().forEach(users::add);

		return users;
	}
}
