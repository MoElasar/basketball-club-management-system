package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class MerchandiseOrderItemService {
	@Autowired
	private MerchandiseOrderItemRepository merchandiseOrderItemRepository;

	public MerchandiseOrderItem saveMerchandiseOrderItem(MerchandiseOrderItem merchandiseOrderItem) {
		return merchandiseOrderItemRepository.save(merchandiseOrderItem);
	}

	public Optional<MerchandiseOrderItem> getMerchandiseOrderItemById(Long id) {
		return merchandiseOrderItemRepository.findById(id);
	}

	public List<MerchandiseOrderItem> getAllMerchandiseOrderItems() {
		return merchandiseOrderItemRepository.findAll();
	}

	public void deleteMerchandiseOrderItem(Long id) {
		merchandiseOrderItemRepository.deleteById(id);
	}

	public List<MerchandiseOrderItem> findMerchandiseOrderItemsByOrder(MerchandiseOrder order) {
		return merchandiseOrderItemRepository.findByOrder(order);
	}

	public List<MerchandiseOrderItem> findMerchandiseOrderItemsByMerchandise(Merchandise merchandise) {
		return merchandiseOrderItemRepository.findByMerchandise(merchandise);
	}
}