package com.innovTech.controller;

import java.util.List;
import java.util.Map;
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

import com.innovTech.controller.model.BankEmployee;
import com.innovTech.entity.BankApiResponse;
import com.innovTech.entity.Employee;
import com.innovTech.entity.User;
import com.innovTech.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("bank/{bankId}/employee")
@Slf4j
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	
	/* ======================= CREATE = POST Employee ============================  */
	
	@PostMapping
	public ResponseEntity<Void> createEmployee(
			@PathVariable Long bankId,
			@RequestBody BankEmployee bankEmployee) {
		
		log.info("Attempting to create an employee {} in bank with ID = .",
				bankEmployee, bankId);
		
		throw new UnsupportedOperationException(
				"Creating an employee is not allowed.");
		
		//Employee savedEmployee = employeeService.saveEmployee(bankId, bankEmployee);
		//return ResponseEntity.ok(savedEmployee);
		
		//return ResponseEntity.noContent().build();
	}
	
	/* ======================= UPDATE = PUT Employee =============================  */
	
	@PutMapping("/{employeeId}")
	public ResponseEntity<BankApiResponse<BankEmployee>> updateEmployee(
			@PathVariable Long bankId,
			@PathVariable Long employeeId,
			@RequestBody BankEmployee bankEmployee) {
		
		bankEmployee.setEmployeeId(employeeId);

		log.info("Updating an employee {} with ID = {} at bank with ID = {}.", employeeId, bankId);
		
		//User user = employee.getUser();

		BankEmployee updatedEmployee = employeeService.updateEmployee(bankId, bankEmployee);
		
		BankApiResponse<BankEmployee> response = new BankApiResponse<BankEmployee>(
				"Employee updated successfully!", updatedEmployee
				);

		return ResponseEntity.ok(response);
		
	}
	
	
	/* ======================= READ = GET Employee ============================  */
	
	@GetMapping
	public ResponseEntity<List<BankEmployee>> retrieveAllEmployeesInBankId(@PathVariable Long bankId) {
		
		log.info("Retrieving all employees in bank with ID = {} ", bankId);
		
		List<BankEmployee> bankEmployees = employeeService.retrieveAllEmployeesInBankId(bankId);
		
		if (!bankEmployees.isEmpty()) {
			return ResponseEntity.ok(bankEmployees);
		} 
		
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@GetMapping("/{employeeId}")
	public ResponseEntity<BankEmployee> retrieveEmployeeById(
			@PathVariable Long bankId, @PathVariable Long employeeId) {
		
		log.info("Retrieving an employee with ID = {} for bank with ID = {} ", 
			employeeId, bankId);
		
		BankEmployee bankEmployee = employeeService.retrieveEmployeeById(bankId, employeeId);
		
		if (!Objects.isNull(bankEmployee)) {
			return ResponseEntity.ok(bankEmployee);
		} 
		
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	/*
	@GetMapping("/{employeeId}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
		
		log.info("Retrieving an employee with ID = {}.", employeeId);
		
		Employee employee = employeeService.getEmployeeById(employeeId);
		
		if (employee != null) {
			return ResponseEntity.ok(employee);
		} 
		
		else {
			return ResponseEntity.notFound().build();
		}
	}
	*/

	/*
	@GetMapping("/employee/username/{username}")
	public ResponseEntity<Employee> getEmployeeByUsername(@PathVariable String username) {
		
		log.info("Retrieving an employee with username = {}.", username);
		
		Employee employee = userService.getEmployeeByUsername(username);
		
		if (employee != null) {
			return ResponseEntity.ok(employee);
		}
		
		else {
			return ResponseEntity.notFound().build();
		}
	}
	*/

	
	/* ======================= DELETE = DELETE Employee ============================  */
	
	@DeleteMapping
	public ResponseEntity<Void> deleteEmployees(@PathVariable Long bankId) {
		
		log.info("Attempting to delete all employees in bank with ID = {}.", bankId);
		
		throw new UnsupportedOperationException(
				"Deleting all employees is not supprted."
				);
	}
	
	@DeleteMapping("/{employeeId}")
	public ResponseEntity<BankApiResponse<Void>> deleteEmployeeById(
			@PathVariable Long bankId,
			@PathVariable Long employeeId) {
		
		log.info("Deleting employee with ID = {} in bank with ID = {} ", employeeId, bankId);
		
		employeeService.deleteEmployeeById(bankId, employeeId);
		
		BankApiResponse<Void> response = new BankApiResponse<Void>(
				"Employee deleted successfully!", null
				);
		
		return ResponseEntity.ok(response);
		
	}
	
	/*
	@DeleteMapping("/{employeeId}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
		
		log.info("Deleting an employee with ID = {}.", employeeId);
		
		employeeService.deleteEmployee(employeeId);
		
		return ResponseEntity.noContent().build();
	}
	*/
	

}
