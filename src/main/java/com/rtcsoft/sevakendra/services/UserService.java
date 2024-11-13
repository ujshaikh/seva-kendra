package com.rtcsoft.sevakendra.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rtcsoft.sevakendra.entities.User;
import com.rtcsoft.sevakendra.enums.AccountStatus;
import com.rtcsoft.sevakendra.exceptions.ApiException;
import com.rtcsoft.sevakendra.repositories.UserRepository;

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

	public void updateStatus(Integer userId, AccountStatus status) throws ApiException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException("Customer not found with id " + userId));
		user.setStatus(status);
		userRepository.save(user);
	}
}
