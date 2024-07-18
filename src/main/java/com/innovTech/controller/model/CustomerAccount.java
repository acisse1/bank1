package com.innovTech.controller.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.innovTech.entity.Account;
import com.innovTech.entity.AccountType;
import com.innovTech.entity.Transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerAccount {

	
private Long accountId;
	
	private String accountNumber;
	
	private BigDecimal accountBalance;
	
	private AccountType accountType;
	
	private Set<AccountTransaction> transactions = new HashSet<AccountTransaction>();
	
	// constructor 
	
	public CustomerAccount(Account account) {

		this.accountId = account.getAccountId();
		this.accountNumber = account.getAccountNumber();
		this.accountBalance = account.getAccountBalance();
		this.accountType = account.getAccountType();
		
		
		for (Transaction transaction : account.getTransactions()) {
			
			AccountTransaction accountTransaction = new AccountTransaction(transaction);
			this.transactions.add(accountTransaction);
		}
	}

}
