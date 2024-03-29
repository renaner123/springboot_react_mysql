package com.springboot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.data.vo.security.AccountCredentialsVO;
import com.springboot.services.AuthServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

	private Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthServices authServices;

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Authenticates a user and returns a token")
	@PostMapping(value = "/signin")
	public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {

		logger.info("Authenticating user " + data.getUsername());

		if (checkIfParamsIsNotNull(data)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		}
		var token = authServices.signin(data);

		logger.info("Token: " + token);

		if (token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username/password supplied");
		}

		return token;
	}

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Refreshe token for authenticated usre and returns a new token")
	@PutMapping(value = "/refresh/{username}")
	public ResponseEntity refreshToken(@PathVariable("username") String username,
			@RequestHeader("Authorization") String refreshToken) {

		logger.info("Refresh a token for user " + username);

		if (checkIfParamsIsNotNull(username, refreshToken)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		}
		var token = authServices.refreshToken(username, refreshToken);

		logger.info("Token: " + token);

		if (token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username/password supplied");
		}

		return token;
	}

	private boolean checkIfParamsIsNotNull(String username, String refreshToken) {
		return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
	}

	private boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
		return data == null || data.getUsername() == null || data.getUsername().isBlank() || data.getPassword() == null
				|| data.getPassword().isBlank();
	}
}
