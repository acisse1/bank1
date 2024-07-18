package com.innovTech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovTech.entity.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
