package com.innovTech.controller.model;

import com.innovTech.entity.Employee;
import com.innovTech.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BankEmployee {
	
	
	private Long employeeId;
	
	private String employeeFirstName;
	
	private String employeeLastName;
	
	private String employeeEmail;
	
	private String employeePhone;
	
	private String employeeJobTitle;
	
	private User user;
	
	// constructor 
	
	public BankEmployee(Employee employee) {

		this.employeeId = employee.getEmployeeId();
		this.employeeFirstName = employee.getEmployeeFirstName();
		this.employeeLastName = employee.getEmployeeLastName();
		this.employeeEmail = employee.getEmployeeEmail();
		this.employeePhone = employee.getEmployeePhone();
		this.employeeJobTitle = employee.getEmployeeJobTitle();
		
		this.user = employee.getUser();
	}

}
