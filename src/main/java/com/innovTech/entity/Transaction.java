package com.innovTech.entity;

import java.math.BigDecimal;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Transaction {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private Long transactionId;
	
	@Column(name = "transaction_amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal transactionAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false)
	private TransactionType transactionType;
	
	
	@Column(name = "transaction_date", nullable = false)
	private String transactionDate;
	
	@ManyToOne(cascade = CascadeType.PERSIST)	// I just change cascade type from "ALL" to "PERSIST"
	@JoinColumn(name = "account_id")			// "ALL" delete the parent = account record.
	@EqualsAndHashCode.Exclude					// "PERSIST" only delete the transaction.
	@ToString.Exclude
	private Account account;

}
