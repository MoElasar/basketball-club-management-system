package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Customer;
import com.learning.main.model.TicketPurchase;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketPurchaseRepository extends JpaRepository<TicketPurchase, Long> {
	List<TicketPurchase> findByCustomer(Customer customer);

	List<TicketPurchase> findByPurchaseDateTimeBetween(LocalDateTime start, LocalDateTime end);
}