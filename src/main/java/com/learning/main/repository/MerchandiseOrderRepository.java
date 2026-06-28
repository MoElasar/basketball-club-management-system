package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.enm.OrderStatus;
import com.learning.main.model.Customer;
import com.learning.main.model.MerchandiseOrder;
import java.time.LocalDateTime;
import java.util.List;

public interface MerchandiseOrderRepository extends JpaRepository<MerchandiseOrder, Long> {
	List<MerchandiseOrder> findByCustomer(Customer customer);

	List<MerchandiseOrder> findByStatus(OrderStatus status);

	List<MerchandiseOrder> findByOrderDateTimeBetween(LocalDateTime start, LocalDateTime end);
}