package com.innovTech.controller.model;

import java.util.HashSet;
import java.util.Set;

import com.innovTech.entity.Bank;
import com.innovTech.entity.Customer;
import com.innovTech.entity.Employee;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BankDto {

	
private Long bankId;
	
	private String bankAddress;
	
	private String bankCity;
	
	private String bankState;
	
	private String bankZip;
	
	private String bankPhone;
	
	private Set<BankEmployee> employees = new HashSet<BankEmployee>();
	
	private Set<BankCustomer> customers = new HashSet<BankCustomer>();

	
	// constructor
	
	public BankDto(Bank bank) {
		super();
		
		this.bankId = bank.getBankId();
		this.bankAddress = bank.getBankAddress();
		this.bankCity = bank.getBankCity();
		this.bankState = bank.getBankState();
		this.bankZip = bank.getBankZip();
		this.bankPhone = bank.getBankPhone();
		
		for (Employee employee : bank.getEmployees()) {
			
			BankEmployee bankEmployee = new BankEmployee(employee);
			this.employees.add(bankEmployee);
		}
		
		
		for (Customer customer : bank.getCustomers()) {
			
			BankCustomer bankCustomer = new BankCustomer(customer);
			this.customers.add(bankCustomer);
		}
	}
	
}
