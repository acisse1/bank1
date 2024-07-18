package com.innovTech.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innovTech.controller.model.CustomerAccount;
import com.innovTech.entity.Account;
import com.innovTech.entity.Bank;
import com.innovTech.entity.Customer;
import com.innovTech.repository.AccountRepository;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private BankService bankService;
	
	
	/* ====================== Method to generate a unique 10-digit account number ============== */
	
	@Transactional(readOnly = false)
    public String generateAccountNumber() {
    	
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder(10);
        Random random = new Random();
        
        for (int i = 0; i < 10; i++) {
            sb.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        
        return sb.toString();
    }
	
	
	/* =================== implementing saveAccount() =======================  */

	public CustomerAccount saveAccount(Long bankId, Long customerId, CustomerAccount customerAccount) {

		
		customerAccount.setAccountNumber(generateAccountNumber()); // unique 10-digits account number.
    	
    	Bank bank = bankService.findBankById(bankId);
    	Customer customer = customerService.findCustomerById(customerId, bankId);
    	Long accountId = customerAccount.getAccountId();
    	
    	// check account balance to be more than zero.
		
		 if (Objects.isNull(customerAccount.getAccountBalance())) {
		 
			 throw new DataIntegrityViolationException("Account balance field must not be null.");
		 }
		 
    	
    	Account account = findOrCreateAccount(accountId);
    	
    	setFieldsInAccount(account, customerAccount);
    	
    	account.setCustomer(customer);
    	customer.getAccounts().add(account);
    	
    	Account dbAccount = accountRepository.save(account);
    	CustomerAccount cutomerAccountDto = new CustomerAccount(dbAccount);
    	
    	
    	return cutomerAccountDto;
	}

	
	/* ========================= implementing setFieldsInAccount() ===============================  */

	private void setFieldsInAccount(Account account, CustomerAccount customerAccount) {

		account.setAccountId(customerAccount.getAccountId());
    	account.setAccountNumber(customerAccount.getAccountNumber());
    	account.setAccountBalance(customerAccount.getAccountBalance());
    	account.setAccountType(customerAccount.getAccountType());
    
	}

	/* ========================= implementing findOrCreateAccount() ===============================  */

	private Account findOrCreateAccount(Long accountId) {

		Account account;
		
		//if (Objects.isNull(account))
		
		if (Objects.isNull(accountId)) {
			account = new Account();
		}
		
		else {
			account = findAccountById(accountId);
		}
		
		return account;
		
	}


	/* ========================= implementing findAccountById() ===============================  */
	
	public Account findAccountById(Long accountId) {
		
		return accountRepository.findById(accountId).orElseThrow(
				() -> new NoSuchElementException(
						"Account with ID = " + accountId + " wsa not found"
						));
	}


	/* ========================= implementing updateAccount() ===============================  */
	
	public CustomerAccount updateAccount(Long bankId, Long customerId, Long accountId,
			CustomerAccount customerAccount) {

		customerAccount.setAccountId(accountId);
		Customer customer = customerService.findCustomerById(customerId, bankId);
		
		Account account = findAccountById(accountId);
		
		//setFieldsInAccount(account, customerAccount);
		
		account.setAccountId(customerAccount.getAccountId());
    	//account.setAccountNumber(customerAccount.getAccountNumber());	// account number should not change.
    	account.setAccountBalance(customerAccount.getAccountBalance());
    	account.setAccountType(customerAccount.getAccountType());
		
		account.setCustomer(customer);
    	customer.getAccounts().add(account);
    	
    	Account dbAccount = accountRepository.save(account);
    	CustomerAccount cutomerAccountDto = new CustomerAccount(dbAccount);
		
		return cutomerAccountDto;
	}


	/* ========================= implementing deleteAccountById() ===============================  */

	@Transactional(readOnly = false)
	public void deleteAccountById(Long accountId, Long customerId, Long bankId) {

		Account account = findAccountById(accountId);
		
		Customer customer = customerService.findCustomerById(customerId, bankId);
		
		if (account.getCustomer().getCustomerId() != customerId) {
			
			throw new IllegalStateException(
					"Account with ID = " + accountId + " was not owned by bank with ID = "
					+ bankId);
		}
		
		customer.getAccounts().remove(account);
		
		accountRepository.delete(account);
	}
	
	
	/* ================= implementing retrieveAllAccountsForCustomerIdd() ========================  */

	@Transactional(readOnly = true)
	public List<CustomerAccount> retrieveAllAccountsForCustomerId(Long bankId, Long customerId) {

		Customer customer = customerService.findCustomerById(customerId, bankId);
		
		CustomerAccount customerAccount;
		
		List<CustomerAccount> customerAccounts = new ArrayList<CustomerAccount>();
		
		for (Account account : customer.getAccounts()) {
			
			customerAccount = new CustomerAccount(account);
			customerAccounts.add(customerAccount);
		}
		return customerAccounts;
	}

	
	@Transactional(readOnly = true)
	public CustomerAccount retrieveAccountById(Long bankId, Long customerId, Long accountId) {

		Account account = findAccountById(accountId);
		
		Customer customer = customerService.findCustomerById(customerId, bankId);
		
		if (account.getCustomer().getCustomerId() != customerId) {
			
			throw new IllegalStateException(
					"Account with ID = " + accountId + " was not owned by customer with "
					+ " ID = " + customerId + " in bank with ID = " + bankId);
		}
		
		CustomerAccount customerAccount = new CustomerAccount(account);
		
		return customerAccount;
	}


}
