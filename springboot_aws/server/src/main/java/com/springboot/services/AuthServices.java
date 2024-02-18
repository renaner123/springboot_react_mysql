package com.springboot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import com.springboot.data.vo.security.AccountCredentialsVO;
import com.springboot.data.vo.security.TokenVO;
import com.springboot.repositories.UserRepository;
import com.springboot.security.jwt.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;

@Service
public class AuthServices {

	private Logger logger = LoggerFactory.getLogger(AuthServices.class);

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository repository;

	@SuppressWarnings("rawtypes")
	public ResponseEntity signin(AccountCredentialsVO data) {
		try {
			var username = data.getUsername();
			var password = data.getPassword();

			logger.info("Starting the autheticate process");

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			logger.info("Finding a userByName " + username);

			var user = repository.findByUsername(username);

			logger.info("User " + username + " successfully located");

			var tokenResponse = new TokenVO();

			if (user != null) {
				tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Username " + username + " not found");
			}

			return ResponseEntity.ok(tokenResponse);
		} catch (Exception e) {
			logger.error("Error trying to sign in user ", e);
			throw new BadCredentialsException("Invalid username/password supplied");
		}
	}

	@SuppressWarnings("rawtypes")
	public ResponseEntity refreshToken(String username, String refreshToken) {
		var user = repository.findByUsername(username);

		var tokenResponse = new TokenVO();
		if (user != null) {
			tokenResponse = tokenProvider.refreshAccessToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found");
		}
		return ResponseEntity.ok(tokenResponse);
	}
}
