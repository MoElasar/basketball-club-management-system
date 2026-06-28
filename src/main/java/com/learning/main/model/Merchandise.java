package com.learning.main.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.learning.main.enm.GameStatus;
import com.learning.main.enm.MediaType;
import com.learning.main.enm.MerchandiseCategory;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name = "merchandise")
public class Merchandise {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long merchandiseId;

	private String itemName;

	@Enumerated(EnumType.STRING)
	private MerchandiseCategory category;

	private double price;
	private int stockQuantity;
	private String description;
	private String imageUrl;

	@OneToMany(mappedBy = "merchandise")
	private Set<MerchandiseOrderItem> orderItems;

	public Long getMerchandiseId() {
		return merchandiseId;
	}

	public void setMerchandiseId(Long merchandiseId) {
		this.merchandiseId = merchandiseId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public MerchandiseCategory getCategory() {
		return category;
	}

	public void setCategory(MerchandiseCategory category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Set<MerchandiseOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<MerchandiseOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Merchandise() {
		super();
		// TODO Auto-generated constructor stub
	}

}
