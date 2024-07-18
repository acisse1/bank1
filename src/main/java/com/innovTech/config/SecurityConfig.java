package com.innovTech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.innovTech.filter.JwtAuthenticationFilter;
import com.innovTech.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	 @Autowired
	 private AccessDeniedHandler customAccessDeniedHandler;
	
	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
		
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(req -> req
					.requestMatchers("/authenticate/**", "/register/**").permitAll()
					//.requestMatchers("/bank/**", "/bank/**{bankId}").hasAuthority("EMPLOYEE")
					.requestMatchers("/bank/{bankId}/employee/{employeeId}").hasAuthority("EMPLOYEE")
					.requestMatchers("/account/**", "/transaction/**").authenticated()
					.requestMatchers("/employee/**").hasAuthority("EMPLOYEE")
					.requestMatchers("/bank/{bankId}/customer/{customerId}").hasAuthority("CUSTOMER")
					.requestMatchers("/customer/**", "/customer/{customerId}").hasAuthority("CUSTOMER")
					.requestMatchers("/account/{accountId}", "/transaction/{transactionId}").authenticated()
					.anyRequest().authenticated()
				)
				.exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler)) // newly added to display 
				.userDetailsService(userDetailsServiceImpl)										// custom message for 403 forbidden
				.sessionManagement(session -> session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder () {
		
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager (
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		
		return authenticationConfiguration
				.getAuthenticationManager();
	}

}
