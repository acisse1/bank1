package com.innovTech.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innovTech.controller.model.AccountTransaction;
import com.innovTech.entity.Account;
import com.innovTech.entity.Bank;
import com.innovTech.entity.Customer;
import com.innovTech.entity.Transaction;
import com.innovTech.entity.TransactionType;
import com.innovTech.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private BankService bankService;
	
	
	/* ========================= implementing saveTransaction() ===================================  */

	@Transactional(readOnly = false)
	public AccountTransaction saveTransaction(Long bankId, Long customerId, Long accountId,
			AccountTransaction accountTransaction) {
		
		Bank bank = bankService.findBankById(bankId);
    	Customer customer = customerService.findCustomerById(customerId, bankId);
    	Account account = accountService.findAccountById(accountId);
    	Long transactionId = accountTransaction.getTransactionId();
		
		 // Ensure amount has two decimal places .setScale() for BigDecimal.
		BigDecimal transactionAmount = accountTransaction.getTransactionAmount();
       transactionAmount = transactionAmount.setScale(2, RoundingMode.HALF_EVEN);

        // Adjust the account balance based on the transaction type
        BigDecimal accountBalance = account.getAccountBalance();
        TransactionType transactionType = accountTransaction.getTransactionType();
        
        switch (transactionType) {
        
        case DEPOSIT:
            accountBalance = accountBalance.add(transactionAmount);
            break;
            
        case WITHDRAWAL:
            accountBalance = accountBalance.subtract(transactionAmount);
            break;
            
        case TRANSFER:
            // For transfers, you would handle both debit and credit in this or another method.
            accountBalance = accountBalance.subtract(transactionAmount);
            break;
            
        case PAYMENT:
            accountBalance = accountBalance.subtract(transactionAmount);
            break;
            
        default:
            throw new IllegalArgumentException("Invalid transaction type");
        }

        if (accountBalance.compareTo(BigDecimal.ZERO) < 0) {
        	throw new RuntimeException("Insufficient balance");
        }
        
        account.setAccountBalance(accountBalance);
        //accountRepository.save(account);
        
        // Transaction time formatter
        
        LocalDateTime date = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("EEEE, d MMMM yyyy, HH:mm", Locale.ENGLISH);

        String formattedDateTime = date.format(formatter);
        
        // End of Transaction time formatter
        
        accountTransaction.setTransactionType(transactionType);
        accountTransaction.setTransactionAmount(transactionAmount);;
        accountTransaction.setTransactionDate(formattedDateTime);
    	
        // End of adjusting the account balance based on the transaction type.
        
    	
    	Transaction transaction = findOrCreateTransaction(transactionId);
    	
    	setFieldsInTransaction(transaction, accountTransaction);
    	
    	transaction.setAccount(account);
    	account.getTransactions().add(transaction);
    	
    	Transaction dbTransaction = transactionRepository.save(transaction);
    	AccountTransaction accountTransactionDto = new AccountTransaction(dbTransaction);
		
		return accountTransactionDto;
	}
	
	 /* =================== implementing setFieldsInTransaction() =======================  */
	
	private void setFieldsInTransaction(Transaction transaction, AccountTransaction accountTransaction) {

		transaction.setTransactionId(accountTransaction.getTransactionId());
		transaction.setTransactionAmount(accountTransaction.getTransactionAmount());
		transaction.setTransactionType(accountTransaction.getTransactionType());
		transaction.setTransactionDate(accountTransaction.getTransactionDate());
	}

	/* =================== implementing findOrCreateTransaction() ===========================  */


	private Transaction findOrCreateTransaction(Long transactionId) {

		Transaction transaction;
		
		if (Objects.isNull(transactionId)) {
			transaction = new Transaction();
		}
		
		else {
			transaction = findTransactionById(transactionId);
		}
		return transaction;
	}
	
	/* ===================== implementing findTransactionById() ============================  */

	private Transaction findTransactionById(Long transactionId) {

		return transactionRepository.findById(transactionId).orElseThrow(
				() -> new NoSuchElementException(
						"Transaction with ID = " + transactionId + " was not found"
						));
	}
	
	
	/* ===================== implementing deleteTransactionById() ============================  */

	@Transactional(readOnly = false)
	public void deleteTransactionById(Long bankId, Long customerId, Long accountId, Long transactionId) {

		Transaction transaction = findTransactionById(transactionId);
		Account account = accountService.findAccountById(accountId);
		
		if (transaction.getAccount().getAccountId() != accountId) {
			
			throw new IllegalStateException(
					"Transaction with ID = " + transactionId + " was not owned by account with ID = "
					+ accountId);
		}
		
		 // Adjust the account balance based on the transaction type
		
		BigDecimal transactionAmount = transaction.getTransactionAmount();
		TransactionType transactionType = transaction.getTransactionType();
		BigDecimal accountBalance = account.getAccountBalance();
		
		switch (transactionType) {
        
        case DEPOSIT:
            accountBalance = accountBalance.subtract(transactionAmount);
            break;
            
        case WITHDRAWAL:
            accountBalance = accountBalance.add(transactionAmount);
            break;
            
        case TRANSFER:
            // For transfers, you would handle both debit and credit in this or another method.
            accountBalance = accountBalance.add(transactionAmount);
            break;
            
        case PAYMENT:
            accountBalance = accountBalance.add(transactionAmount);
            break;
            
        default:
            throw new IllegalArgumentException("Invalid transaction type");
        }

        if (accountBalance.compareTo(BigDecimal.ZERO) < 0) {
        	throw new RuntimeException("Insufficient balance");
        }
        
        account.setAccountBalance(accountBalance);
        
        // End of adjusting the account balance based on the transaction type
		
		account.getTransactions().remove(transaction);
		transactionRepository.delete(transaction);
	}
	
	/* ===================== implementing retrieveAllTransactionInAccountId() ============================  */

	@Transactional(readOnly = true)
	public List<AccountTransaction> retrieveAllTransactionInAccountId(
			Long bankId, Long customerId, Long accountId) {
		
		Account account = accountService.findAccountById(accountId);
		
		List<AccountTransaction> accountTransactions = new ArrayList<AccountTransaction>();
		
		AccountTransaction accountTransaction;
		
		for (Transaction transaction : account.getTransactions()) {
			
			accountTransaction = new AccountTransaction(transaction);
			
			accountTransactions.add(accountTransaction);
		}
		
		return accountTransactions;
	}

	@Transactional(readOnly = true)
	public AccountTransaction retrieveTransactionById(
			Long bankId, Long customerId, Long accountId, Long transactionId) {

		Transaction transaction = findTransactionById(transactionId);
		
		Account account = accountService.findAccountById(accountId);
		
		if (transaction.getAccount().getAccountId() != accountId) {
			
			throw new IllegalStateException(
					"Transaction with ID = " + transactionId + " was not owned by account with ID = "
					+ accountId + " for customer with ID = " + customerId + " in bank with ID = " + bankId);
		}
		
		return new AccountTransaction(transaction);
	}

}
