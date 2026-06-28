package com.learning.main.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.learning.main.enm.GameStatus;
import com.learning.main.enm.MediaType;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name = "merchandise_order_items")
public class MerchandiseOrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderItemId;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private MerchandiseOrder order;

	@ManyToOne
	@JoinColumn(name = "merchandise_id")
	private Merchandise merchandise;

	private int quantity;
	private double unitPrice;

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public MerchandiseOrder getOrder() {
		return order;
	}

	public void setOrder(MerchandiseOrder order) {
		this.order = order;
	}

	public Merchandise getMerchandise() {
		return merchandise;
	}

	public void setMerchandise(Merchandise merchandise) {
		this.merchandise = merchandise;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public MerchandiseOrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}

}
