package com.learning.main.repository.services;

import com.learning.main.enm.MerchandiseCategory;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class MerchandiseService {
	@Autowired
	private MerchandiseRepository merchandiseRepository;

	public Merchandise saveMerchandise(Merchandise merchandise) {
		return merchandiseRepository.save(merchandise);
	}

	public Optional<Merchandise> getMerchandiseById(Long id) {
		return merchandiseRepository.findById(id);
	}

	public List<Merchandise> getAllMerchandise() {
		return merchandiseRepository.findAll();
	}

	public void deleteMerchandise(Long id) {
		merchandiseRepository.deleteById(id);
	}

	public List<Merchandise> findMerchandiseByCategory(MerchandiseCategory category) {
		return merchandiseRepository.findByCategory(category);
	}

	public List<Merchandise> findMerchandiseByPriceBetween(double minPrice, double maxPrice) {
		return merchandiseRepository.findByPriceBetween(minPrice, maxPrice);
	}

	public List<Merchandise> findMerchandiseByStockQuantityGreaterThan(int quantity) {
		return merchandiseRepository.findByStockQuantityGreaterThan(quantity);
	}
}