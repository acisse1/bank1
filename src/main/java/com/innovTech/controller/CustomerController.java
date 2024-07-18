package com.innovTech.controller;

import java.util.List;
import java.util.Objects;

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
import com.innovTech.entity.BankApiResponse;
import com.innovTech.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bank/{bankId}/customer")
@Slf4j
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	
	/* ======================= CREATE = POST Customer ============================  */
	
	@PostMapping
	public ResponseEntity<Void> createCustomer(
			@PathVariable Long bankId,
			@RequestBody BankCustomer bankCustomer) {
		
		log.info("Attempting to create a customer {} in bank with ID = .",
				bankCustomer, bankId);
		
		throw new UnsupportedOperationException(
				"Creating a customer is not allowed.");
		
		//Employee savedEmployee = employeeService.saveEmployee(bankId, bankEmployee);
		//return ResponseEntity.ok(savedEmployee);
		
		//return ResponseEntity.noContent().build();
	}
	
/* ======================= UPDATE = PUT Employee =============================  */
	
	@PutMapping("/{customerId}")
	public ResponseEntity<BankApiResponse<BankCustomer>> updateCustomer(
			@PathVariable Long bankId,
			@PathVariable Long customerId,
			@RequestBody BankCustomer bankCustomer) {
		
		bankCustomer.setCustomerId(customerId);

		log.info("Updating a customer {} with ID = {} at bank with ID = {}.", 
				bankCustomer, customerId, bankId);
		
		//User user = employee.getUser();

		BankCustomer updatedCustomer = customerService.updateCustomer(bankId, bankCustomer);
		
		BankApiResponse<BankCustomer> response = new BankApiResponse<BankCustomer>(
				"Customer updated successfully!", updatedCustomer
				);

		return ResponseEntity.ok(response);
		
	}
	
	
/* ======================= READ = GET Customer ============================  */
	
	@GetMapping
	public ResponseEntity<List<BankCustomer>> retrieveAllCustomersInBankId(@PathVariable Long bankId) {
		
		log.info("Retrieving all customers in bank with ID = {} ", bankId);
		
		List<BankCustomer> bankCustomers = customerService.retrieveAllCustomersInBankId(bankId);
		
		if (!bankCustomers.isEmpty()) {
			return ResponseEntity.ok(bankCustomers);
		} 
		
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@GetMapping("/{customerId}")
	public ResponseEntity<BankCustomer> retrieveCustomerById(
			@PathVariable Long bankId, @PathVariable Long customerId) {
		
		log.info("Retrieving a customer with ID = {} for bank with ID = {} ", 
			customerId, bankId);
		
		BankCustomer bankCustomer = customerService.retrieveCustomerById(bankId, customerId);
		
		if (!Objects.isNull(bankCustomer)) {
			return ResponseEntity.ok(bankCustomer);
		} 
		
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
/* ======================= DELETE = DELETE Customer ============================  */
	
	@DeleteMapping
	public ResponseEntity<Void> deleteAllCustomers(@PathVariable Long bankId) {
		
		log.info("Attempting to delete all customers in bank with ID = {}.", bankId);
		
		throw new UnsupportedOperationException(
				"Deleting all customers is not supprted."
				);
	}
	
	@DeleteMapping("/{customerId}")
	public ResponseEntity<BankApiResponse<Void>> deleteCustomerById(
			@PathVariable Long bankId,
			@PathVariable Long customerId) {
		
		log.info("Deleting customer with ID = {} in bank with ID = {} ", customerId, bankId);
		
		customerService.deleteCustomerById(bankId, customerId);
		
		BankApiResponse<Void> response = new BankApiResponse<Void>(
				"Customer deleted successfully!", null
				);
		
		return ResponseEntity.ok(response);
		
	}
	

}
