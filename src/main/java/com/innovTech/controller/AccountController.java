package com.innovTech.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovTech.controller.model.BankCustomer;
import com.innovTech.controller.model.CustomerAccount;
import com.innovTech.entity.BankApiResponse;
import com.innovTech.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bank/{bankId}/customer/{customerId}/account")
@Slf4j
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	/* ======================= CREATE = POST Account ============================  */
	
	@PostMapping
	public ResponseEntity<BankApiResponse<CustomerAccount>> insertAccount (
			@PathVariable Long bankId,
			@PathVariable Long customerId,
			@RequestBody CustomerAccount customerAccount) {
		
		log.info("Creating an account {} for customer with ID = {} for bank with ID = {} ", 
				customerAccount, customerId, bankId);
		
		CustomerAccount insertedAccount = accountService.saveAccount(bankId, customerId, customerAccount);
		
		BankApiResponse<CustomerAccount> response = new BankApiResponse<CustomerAccount>(
				"Account created successfully!", insertedAccount
				);

		return ResponseEntity.ok(response);
	}
	
	
/* ======================= UPDATE = PUT Account =============================  */
	
	@PutMapping("/{accountId}")
	public ResponseEntity<BankApiResponse<CustomerAccount>> updateAccount (
			@PathVariable Long bankId,
			@PathVariable Long customerId,
			@PathVariable Long accountId,
			@RequestBody CustomerAccount customerAccount) {
		
		
		log.info("Updating an account {} for customer with ID = {} in bank with ID = {} ", 
				customerAccount, customerId, bankId);	
		
		CustomerAccount updatedAccount = accountService.updateAccount(bankId, customerId, accountId, customerAccount);
		
		BankApiResponse<CustomerAccount> response = new BankApiResponse<CustomerAccount>(
				"Account updated successfully!", updatedAccount
				);

		return ResponseEntity.ok(response);
	}
	
	
/* ======================= DELETE = DELETE Account =============================  */
	
	
	@DeleteMapping
	public void deleteAllAccounts(
			@PathVariable Long bankId,
			@PathVariable Long customerId) {
		
		log.info("Attempting to delete all accounts for customer with ID = {} in bank with ID = {}.",
				customerId, bankId);
		
		throw new UnsupportedOperationException(
				"Deleting all accounts is not supported.");
	}
	
	
	@DeleteMapping("/{accountId}")
	public Map<String, String> deleteAccountById(
			@PathVariable Long bankId,
			@PathVariable Long customerId,
			@PathVariable Long accountId) {
		
		log.info("Deleting account with ID = {} for customer with ID = {} in bank with ID = {}.",
				accountId, customerId, bankId);
		
		accountService.deleteAccountById(accountId, customerId, bankId);
		
		return Map.of(
				"message", "Deleting account with ID = " + accountId +
				" for customer with ID = " + customerId + " in bank with ID = " +
				bankId + " was successful."
				);
	}
	
	
/* ======================= READ = GET Account ==================================  */
	
	@GetMapping
	public List<CustomerAccount> retrieveAllAccountsForCustomerId (
			@PathVariable Long bankId,
			@PathVariable Long customerId) {
		
		log.info("Retrieving all accounts for customer with ID = {} in bank with ID = {} ",
				customerId, bankId);
		
		return accountService.retrieveAllAccountsForCustomerId(bankId, customerId);
		
	}
	
	
	@GetMapping("/{accountId}")
	public CustomerAccount retrieveAccountById (
			@PathVariable Long bankId,
			@PathVariable Long customerId,
			@PathVariable Long accountId) {
		
		log.info("Retrieving an account with ID = {} for customer with ID = {} in bank with ID = {} ",
				accountId, customerId, bankId);
		
		return accountService.retrieveAccountById(bankId, customerId, accountId);
		
	}

}
