package com.innovTech.service;


import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.innovTech.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtService {
	
	private final String SECRET_KEY = "0e91d632ced8360287d51bc997825a33827c907fcc1e7d0fa211e0d8129a687c";
	
	
	public String generateToken (User user) {
		
		String token = Jwts
				.builder()
				.subject(user.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
				.signWith(getSigninKey())
				.compact();
		
		return token;
	}


	/*	==================	implementing getSigninKey()	==================	*/
	
	public SecretKey getSigninKey() {

		byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	
	// Payload
	
	public Claims extractAllClaims (String token) {
		
		return Jwts
				.parser()
				.verifyWith(getSigninKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	
	public <T> T extractClaim (String token, Function<Claims, T> resolver) {
		
		Claims claims = extractAllClaims(token);
		
		return resolver.apply(claims);
	}
	
	
	public String extractUsername (String token) {
		
		return extractClaim(token, Claims::getSubject);
	}
	
	
	public boolean isValid (String token, UserDetails user) {
		
		String username = extractUsername(token);
		
		return (username.equals(user.getUsername())) && !isTokenExpired(token);
	}


	public boolean isTokenExpired(String token) {

		return extractExpiration(token).before(new Date());
	}


	public Date extractExpiration(String token) {

		return extractClaim(token, Claims::getExpiration);
	}

}
