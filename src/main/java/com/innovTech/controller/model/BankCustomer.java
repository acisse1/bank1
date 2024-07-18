package com.innovTech.controller.model;

import java.util.HashSet;
import java.util.Set;

import com.innovTech.entity.Account;
import com.innovTech.entity.Customer;
import com.innovTech.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BankCustomer {
	
private Long customerId;
	
	private String customerFirstName;
	
	private String customerLastName;
	
	private String customerEmail;
	
	private User user;
	
	
	private Set<CustomerAccount> accounts = new HashSet<CustomerAccount>();
	
	// constructor 
	
	public BankCustomer(Customer customer) {

		this.customerId = customer.getCustomerId();
		this.customerFirstName = customer.getCustomerFirstName();
		this.customerLastName = customer.getCustomerLastName();
		this.customerEmail = customer.getCustomerEmail();
		
		this.user = customer.getUser();
		
		
		for (Account account : customer.getAccounts()) {
			
			CustomerAccount customerAccount = new CustomerAccount(account);
			this.accounts.add(customerAccount);
		}
	}

}
