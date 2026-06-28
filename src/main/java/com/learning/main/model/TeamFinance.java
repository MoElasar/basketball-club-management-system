package com.learning.main.model;

import java.time.LocalDate;
 
import com.learning.main.enm.PaymentMethod;
import com.learning.main.enm.TransactionType;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

// This class represents a core business entity, holding data and relationships relevant to team finances.
// Data Mapper Pattern (via JPA annotations)
// JPA annotations are used to map this object to the 'team_finances' table in the database.

@Entity
@Table(name = "team_finances")
public class TeamFinance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recordId;

	private LocalDate recordDate;

	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	private String category;
	private double amount;
	private String description;

	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	// Constructors, getters, setters
}