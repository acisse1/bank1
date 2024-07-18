package com.innovTech.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Customer {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long customerId;
	
	@Column(name = "customer_first_name")
	private String customerFirstName;
	
	@Column(name = "customer_last_name")
	private String customerLastName;
	
	@Column(name = "customer_email")
	private String customerEmail;
	
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	
	
	@ManyToMany(mappedBy = "customers", cascade = CascadeType.PERSIST)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Bank> banks = new HashSet<Bank>();
	
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, 
			orphanRemoval = true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Account> accounts = new HashSet<Account>();
	
	
}
