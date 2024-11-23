package com.demoapp.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.demoapp.userservice.dto.UserDto;
import com.demoapp.userservice.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
		log.info("create-user-request: {}", user.toString());
		return ResponseEntity.ok(userService.saveUser(user));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
		log.info("get-user-request for id: {}", id);
		return userService.getUserById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> {
					log.warn("User with ID {} not found", id);
					return ResponseEntity.notFound().build();
				});
	}
}
