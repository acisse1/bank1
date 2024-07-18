package com.innovTech.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovTech.controller.model.BankEmployee;
import com.innovTech.entity.Bank;
import com.innovTech.entity.Employee;
import com.innovTech.entity.User;
import com.innovTech.repository.EmployeeRepository;
import com.innovTech.repository.UserRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BankService bankService;
	
	
	/* =================== saveEmployee() =======================  */
	/*
	public Employee saveEmployee(Employee employee) {
		
		
		return employeeRepository.save(employee);
	}
	*/
	
	/* =================== implementing updateEmployee() =======================  */
	
	public BankEmployee updateEmployee(Long bankId, BankEmployee bankEmployee) {
		
		Long employeeId = bankEmployee.getEmployeeId();
		
		Employee existingEmployee = findEmployeeById(employeeId);

		Bank bank = bankService.findBankById(bankId);
		
		User user = existingEmployee.getUser();
		
		setFieldsInEmployee(existingEmployee, bankEmployee);

		//Employee existingEmployee = employeeRepository.findById(employeeId)
        //        .orElseThrow(() -> new NoSuchElementException("Employee with id: " + employeeId + " not found."));
		
		
        
        // Update the fields of the existing employee except userId
		/*
        existingEmployee.setEmployeeFirstName(bankEmployee.getEmployeeFirstName());
        existingEmployee.setEmployeeLastName(bankEmployee.getEmployeeLastName());
        existingEmployee.setEmployeeEmail(bankEmployee.getEmployeeEmail());
        existingEmployee.setEmployeePhone(bankEmployee.getEmployeePhone());
        existingEmployee.setEmployeeJobTitle(bankEmployee.getEmployeeJobTitle());
        */
        // Add other fields to update as needed
		
		existingEmployee.setBank(bank);
		existingEmployee.setUser(user);
		
		bank.getEmployees().add(existingEmployee);
		
		Employee dbEmployee = employeeRepository.save(existingEmployee);
		
		BankEmployee updatedEmployee = new BankEmployee(dbEmployee);

        return updatedEmployee;
	}
	
	/* =================== implementing setFieldsInEmployee() =======================  */

	private void setFieldsInEmployee(Employee existingEmployee, BankEmployee bankEmployee) {
		
		existingEmployee.setEmployeeId(bankEmployee.getEmployeeId());
		existingEmployee.setEmployeeFirstName(bankEmployee.getEmployeeFirstName());
		existingEmployee.setEmployeeLastName(bankEmployee.getEmployeeLastName());
		existingEmployee.setEmployeeEmail(bankEmployee.getEmployeeEmail());
		existingEmployee.setEmployeePhone(bankEmployee.getEmployeePhone());
		existingEmployee.setEmployeeJobTitle(bankEmployee.getEmployeeJobTitle());
	}
	
	/* =================== implementing findEmployeeById() =======================  */

	private Employee findEmployeeById(Long employeeId) {

		return employeeRepository.findById(employeeId).orElseThrow(
				() -> new NoSuchElementException(
						"Employee with ID = " + employeeId + " was not found."
						));
	}

	
	/* =================== findByEmployeeId() =======================  */
	/*
	public Employee getEmployeeById(Long employeeId) {
		
		return employeeRepository.findById(employeeId).orElse(null);
	}
	*/
	
	
	/* =================== deleteEmployeeById() =======================  */
	/*
	public void deleteEmployee(Long employeeId) {
		
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException(
						"Employee not found."));
		
		User user = employee.getUser();
		userRepository.delete(user);
		employeeRepository.deleteById(employeeId);
	}
	*/
	
	public void deleteEmployeeById(Long bankId, Long employeeId) {

		Employee employee = findEmployeeById(employeeId);
		
		Bank bank = bankService.findBankById(bankId);
		
		if (employee.getBank().getBankId() != bankId) {
			
			throw new IllegalStateException(
					"Employee with ID = " + employeeId + " was not owned by bank with ID = "
					+ bankId);
		}
		
		User user = employee.getUser();
		//userRepository.delete(user);
		
		employeeRepository.delete(employee);
	}


	/* =================== implementing retrieveAllEmployeesInBankId() =======================  */
	
	public List<BankEmployee> retrieveAllEmployeesInBankId(Long bankId) {

		Bank bank = bankService.findBankById(bankId);
		
		List<BankEmployee> bankEmployees = new ArrayList<BankEmployee>();
		
		BankEmployee bankEmployee;
		
		for (Employee employee : bank.getEmployees()) {
			
			bankEmployee = new BankEmployee(employee);
			bankEmployees.add(bankEmployee);
		}
		
		return bankEmployees;
		
	}
	
	/* =================== implementing retrieveEmployeeById() =======================  */

	public BankEmployee retrieveEmployeeById(Long bankId, Long employeeId) {

		Employee employee = findEmployeeById(employeeId);
		
		Bank bank = bankService.findBankById(bankId);
		
		if (employee.getBank().getBankId() != bankId) {
			
			throw new IllegalStateException(
					"Employee with ID = " + employeeId + " was not owned by bank with ID = "
					+ bankId);
		}
		
		BankEmployee bankEmployee = new BankEmployee(employee);
		
		return bankEmployee;
	}

}
