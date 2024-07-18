package com.innovTech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.innovTech.entity.AuthenticationResponse;
import com.innovTech.entity.BankApiResponse;
import com.innovTech.entity.Role;
import com.innovTech.entity.User;
import com.innovTech.service.AuthenticationService;

@RestController
public class AuthenticationController {

	
	@Autowired
	private AuthenticationService authenticationService;
	
	/*
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register (
			@RequestBody User request) {
		
		return ResponseEntity.ok(authenticationService
				.register(request));
	}
	*/
	
	@PostMapping("/register")
	public ResponseEntity<BankApiResponse<AuthenticationResponse>> register (
			@RequestBody User request) {
		
		Role role = request.getRole();
		
		AuthenticationResponse authenticationResponse = authenticationService.register(request);
		
		BankApiResponse<AuthenticationResponse> response = new BankApiResponse<AuthenticationResponse>(
				"User registered as " + role + " successfully!", authenticationResponse
				);
		
		return ResponseEntity.ok(response);
	}
	
	
	/*
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate (
			@RequestBody User request) {
		
		return ResponseEntity.ok(authenticationService
				.authenticate(request));
	}
	*/
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<BankApiResponse<AuthenticationResponse>> authenticate (
			@RequestBody User request) {
		
		Role role = request.getRole();
		
		AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
		
		BankApiResponse<AuthenticationResponse> response = new BankApiResponse<AuthenticationResponse>(
				"User authenticad successfully!", authenticationResponse
				);
		
		return ResponseEntity.ok(response);
	}
	
}
