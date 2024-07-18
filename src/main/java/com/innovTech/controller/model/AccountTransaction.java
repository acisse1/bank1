package com.innovTech.controller.model;

import java.math.BigDecimal;

import com.innovTech.entity.Transaction;
import com.innovTech.entity.TransactionType;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class AccountTransaction {

	
	private Long transactionId;
	
	private BigDecimal transactionAmount;
	
	private TransactionType transactionType;
	
	private String transactionDate;
	
	// constructor 
	
	public AccountTransaction(Transaction transaction) {

		this.transactionId = transaction.getTransactionId();
		this.transactionAmount = transaction.getTransactionAmount();
		this.transactionType = transaction.getTransactionType();
		this.transactionDate = transaction.getTransactionDate();
	}
	
}
