package com.learning.main.repository.services;

import com.learning.main.enm.OrderStatus;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class MerchandiseOrderService {
	@Autowired
	private MerchandiseOrderRepository merchandiseOrderRepository;

	public MerchandiseOrder saveMerchandiseOrder(MerchandiseOrder merchandiseOrder) {
		return merchandiseOrderRepository.save(merchandiseOrder);
	}

	public Optional<MerchandiseOrder> getMerchandiseOrderById(Long id) {
		return merchandiseOrderRepository.findById(id);
	}

	public List<MerchandiseOrder> getAllMerchandiseOrders() {
		return merchandiseOrderRepository.findAll();
	}

	public void deleteMerchandiseOrder(Long id) {
		merchandiseOrderRepository.deleteById(id);
	}

	public List<MerchandiseOrder> findMerchandiseOrderByCustomer(Customer customer) {
		return merchandiseOrderRepository.findByCustomer(customer);
	}

	public List<MerchandiseOrder> findMerchandiseOrderByStatus(OrderStatus status) {
		return merchandiseOrderRepository.findByStatus(status);
	}

	public List<MerchandiseOrder> findMerchandiseOrderByOrderDateTimeBetween(LocalDateTime start, LocalDateTime end) {
		return merchandiseOrderRepository.findByOrderDateTimeBetween(start, end);
	}
}
