package com.innovTech.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Bank {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bank_id")
	private Long bankId;
	
	@Column(name = "bank_address")
	private String bankAddress;
	
	@Column(name = "bank_city")
	private String bankCity;
	
	@Column(name = "bank_state")
	private String bankState;
	
	@Column(name = "bank_zip")
	private String bankZip;
	
	@Column(name = "bank_phone")
	private String bankPhone;
	
	
	@OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, 
			orphanRemoval = true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Employee> employees = new HashSet<Employee>();
	
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "bank_customer", 
			joinColumns = @JoinColumn(name = "bank_id"),
			inverseJoinColumns = @JoinColumn(name = "customer_id"))
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Customer> customers = new HashSet<Customer>();
	
}
