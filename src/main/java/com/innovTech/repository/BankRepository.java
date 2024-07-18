package com.innovTech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovTech.entity.Bank;



public interface BankRepository extends JpaRepository<Bank, Long> {

}
