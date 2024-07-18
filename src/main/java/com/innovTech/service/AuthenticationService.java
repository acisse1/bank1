package com.innovTech.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.innovTech.entity.AuthenticationResponse;
import com.innovTech.entity.Customer;
import com.innovTech.entity.Employee;
import com.innovTech.entity.Role;
import com.innovTech.entity.User;
import com.innovTech.repository.CustomerRepository;
import com.innovTech.repository.EmployeeRepository;
import com.innovTech.repository.UserRepository;

@Service
public class AuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	
	public AuthenticationResponse register (User request) {
		
		if (userRepository.existsByUsername(request.getUsername())) {
			
			throw new IllegalArgumentException("Username is already taken");
		}
		
		User user = new User();
		
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());
		
		userRepository.save(user);
		
		if (user.getRole().equals(Role.EMPLOYEE)) {
			
			Employee employee = new Employee();
			
			employee.setUser(user);
			employeeRepository.save(employee);
		}
		
		else if (user.getRole().equals(Role.CUSTOMER)) {
			
			Customer customer = new Customer();
			
			customer.setUser(user);
			customerRepository.save(customer);
		}
		
		String token = jwtService.generateToken(user);
		
		return new AuthenticationResponse(token);
	}
	
	
	
	public AuthenticationResponse authenticate (User request) {
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(), request.getPassword()
						)		
				);
		
		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() ->
					new NoSuchElementException("User Not Found"));
		
		
		String token = jwtService.generateToken(user);
		
		return new AuthenticationResponse(token);
	}

}
