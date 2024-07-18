package com.innovTech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovTech.entity.Transaction;



public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
