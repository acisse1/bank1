package com.innovTech.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Account {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Long accountId;
	
	@Column(name = "account_number", nullable = false, unique = true)
	private String accountNumber;
	
	@Column(name = "accountBalance")
	private BigDecimal accountBalance;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false)
	private AccountType accountType;
	
	@ManyToOne(cascade = CascadeType.PERSIST)	// I just change cascade type from "ALL" to "PERSIST"
	@JoinColumn(name = "customer_id")			// to test delete function. And it works perfect.
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Customer customer;
	
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL,
			orphanRemoval = true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Transaction> transactions = new HashSet<Transaction>();

}
