package com.innovTech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.innovTech.controller.model.BankDto;
import com.innovTech.entity.Bank;
import com.innovTech.entity.BankApiResponse;
import com.innovTech.service.BankService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bank")
@Slf4j
public class BankController {
	
	
	@Autowired
	private BankService bankService;
	
	
/* ======================= CREATE = POST Bank ============================  */
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<BankApiResponse<BankDto>> insertBank (@RequestBody BankDto bankDto) {
		
		log.info("Creating a bank data {} ", bankDto);
		
		 BankDto savedBank = bankService.saveBank(bankDto);
		 
		 BankApiResponse<BankDto> response = new BankApiResponse<BankDto>(
				 "Bank created successfully!", savedBank
				 );
		
		return ResponseEntity.ok(response);
	}
	
	/* ======================= UPDATE = PUT Bank ============================  */
	
	
	@PutMapping("/{bankId}")
	public ResponseEntity<BankApiResponse<BankDto>> updateBank (
			@PathVariable Long bankId, 
			@RequestBody BankDto bankDto) {
		
		bankDto.setBankId(bankId);
		
		log.info("Updating a bank data {} with ID ", bankDto, bankId);
		
		BankDto savedBank = bankService.saveBank(bankDto);
		
		BankApiResponse<BankDto> response = new BankApiResponse<BankDto>(
				 "Bank updated successfully!", savedBank
				 );
		
		return ResponseEntity.ok(response);
	}
	
	/* ======================= READ = GET Bank ============================  */
	
	
	@GetMapping
	public ResponseEntity<List<BankDto>> retrieveAllBanks() {
		
		log.info("Retrieving all banks with employees and customers.");
		
		 List<BankDto> banks = bankService.retrieveAllBanks();
		 
	     return ResponseEntity.ok(banks);
	}
	
	@GetMapping("/{bankId}")
	public ResponseEntity<BankDto> retrieveBankById(@PathVariable Long bankId) {
		
		log.info("Retrieving a bank with ID = {} ", bankId);
		
		
		
		BankDto bankDto = bankService.retrieveBankById(bankId);
		
        if (bankDto != null) {
            return ResponseEntity.ok(bankDto);
        } 
        
        else {
            return ResponseEntity.notFound().build();
        }
		
	}
	
	
	/* ======================= DELETE = DELETE Bank ============================  */
	
	@DeleteMapping
	public void deleteAllBanks() {
		
		log.info("Attempting to delete all banks.");
		
		throw new UnsupportedOperationException(
				"Deleting all banks is not allowed.");
	}
	
	
	@DeleteMapping("/{bankId}")
	public ResponseEntity<BankApiResponse<Void>> deleteBankById(@PathVariable Long bankId) {
		
		log.info("Deleting a bank with ID = {} ", bankId);
		
		bankService.deleteBankById(bankId);
		
		BankApiResponse<Void> response = new BankApiResponse<Void>(
				"Bank deleted successfully!", null
				);
		
        //return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(response);
	}


}
