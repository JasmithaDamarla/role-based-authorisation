package com.authorize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authorize.model.dto.AuthRequest;
import com.authorize.service.implementation.AuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/auth")
@RestController
public class AuthController {
	
	@Autowired
	private AuthService service;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/token")
	public String getToken(@RequestBody AuthRequest authRequest) {
		log.info("enetered auth controller");
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
		log.info(authenticate.toString());
		if (authenticate.isAuthenticated()) {
			log.info(authRequest.getName() +" is authenticated");
			return service.generateToken(authRequest.getName());
		} else {
			throw new RuntimeException("invalid access");
		}
	}

	@GetMapping("/validate")
	public String validateToken(@RequestParam("token") String token) {
		service.validateToken(token);
		return "Token is valid";
	}
}
