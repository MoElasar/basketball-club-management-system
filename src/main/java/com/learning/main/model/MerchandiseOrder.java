package com.learning.main.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.learning.main.enm.OrderStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "merchandise_orders")
public class MerchandiseOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	private LocalDateTime orderDateTime;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private double totalAmount;
	private String shippingAddress;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToMany(mappedBy = "order")
	private Set<MerchandiseOrderItem> items;

	// Constructors
	public MerchandiseOrder() {
	}

	// Getters and Setters
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(LocalDateTime orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<MerchandiseOrderItem> getItems() {
		return items;
	}

	public void setItems(Set<MerchandiseOrderItem> items) {
		this.items = items;
	}
}