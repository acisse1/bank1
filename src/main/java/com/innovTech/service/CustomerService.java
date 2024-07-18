package com.innovTech.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovTech.controller.model.BankCustomer;
import com.innovTech.entity.Bank;
import com.innovTech.entity.Customer;
import com.innovTech.entity.User;
import com.innovTech.repository.CustomerRepository;
import com.innovTech.repository.UserRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BankService bankService;
	
	
	/* =================== implementing updateCustomer() =======================  */

	public BankCustomer updateCustomer(Long bankId, BankCustomer bankCustomer) {

		Long customerId = bankCustomer.getCustomerId();
		
		Customer existingCustomer = findCustomerById(customerId, bankId);
		
		Bank bank = bankService.findBankById(bankId);
		
		User user = existingCustomer.getUser();
		
		setFieldsInCustomer(existingCustomer, bankCustomer);
		
		existingCustomer.getBanks().add(bank);
		existingCustomer.setUser(user);
		
		bank.getCustomers().add(existingCustomer);
		
		Customer dbCustomer = customerRepository.save(existingCustomer);
		
		BankCustomer updatedCustomer = new BankCustomer(dbCustomer);
		
		return updatedCustomer;
	}


	/* =================== implementing setFieldsInCustomer() =======================  */
	
	private void setFieldsInCustomer(Customer existingCustomer, BankCustomer bankCustomer) {

		existingCustomer.setCustomerId(bankCustomer.getCustomerId());
		existingCustomer.setCustomerFirstName(bankCustomer.getCustomerFirstName());
		existingCustomer.setCustomerLastName(bankCustomer.getCustomerLastName());
		existingCustomer.setCustomerEmail(bankCustomer.getCustomerEmail());
	}

	/* =================== implementing findCustomerById() =======================  */

	public Customer findCustomerById(Long customerId, Long bankId) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException(
						"Customer with ID = " + customerId + " was not found."
						));
		/*
		boolean found = false;
		
		for (Bank bank : customer.getBanks()) {
			
			if (bank.getBankId() == bankId) {
				found = true;
				break;
			}
		}
		
		if (!found) {
			throw new IllegalArgumentException(
					"Customer with ID = " + customerId + " is not a member of "
							+ "bank with ID = " + bankId);
		}
		*/
		
		return customer;
	}


	/* =================== implementing retrieveAllCustomersInBankId() =======================  */
	
	public List<BankCustomer> retrieveAllCustomersInBankId(Long bankId) {

		Bank bank = bankService.findBankById(bankId);
		
		List<BankCustomer> bankCustomers = new ArrayList<BankCustomer>();
		
		BankCustomer bankCustomer;
		
		for (Customer customer : bank.getCustomers()) {
			
			bankCustomer = new BankCustomer(customer);
			bankCustomers.add(bankCustomer);
		}
		
		return bankCustomers;
	}

	/* =================== implementing retrieveCustomerById() =======================  */

	public BankCustomer retrieveCustomerById(Long bankId, Long customerId) {

		Customer customer = findCustomerById(customerId, bankId);
		
		Bank bank = bankService.findBankById(bankId);
		
		if (!bank.getCustomers().contains(customer)) {
			
			throw new IllegalStateException(
					"Customer with ID = " + customerId + " was not owned by bank with ID = "
					+ bankId);
		}
		
		BankCustomer bankCustomer = new BankCustomer(customer);
		
		return bankCustomer;
	}

	/* =================== implementing deleteCustomerById() =======================  */
	
	public void deleteCustomerById(Long bankId, Long customerId) {

		Customer customer = findCustomerById(customerId, bankId);
		
		Bank bank = bankService.findBankById(bankId);
		
		if (!customer.getBanks().contains(bank)) {
			
			throw new IllegalStateException(
					"Customer with ID = " + customerId + " was not owned by bank with ID = "
					+ bankId);
		}
		
		User user = customer.getUser();
		//userRepository.delete(user);
		
		bank.getCustomers().remove(customer);
		customerRepository.delete(customer);
		
	}

}
