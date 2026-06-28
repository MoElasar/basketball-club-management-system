package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//Service Layer Pattern
	// This class acts as a service layer providing business logic for Customer entities.

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	public Customer saveCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	public Optional<Customer> getCustomerById(Long id) {
		return customerRepository.findById(id);
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public void deleteCustomer(Long id) {
		customerRepository.deleteById(id);
	}

	public Optional<Customer> findCustomerByUser(User user) {
		return customerRepository.findByUser(user);
	}

	public List<Customer> findCustomersByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

	public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findByUser(User user) {
        return customerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Customer not found for user: " + user.getUsername()));
    }

    public List<TicketPurchase> getCustomerPurchases(Long customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getPurchases)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId))
                .stream()
                .toList();
    }

   

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
}