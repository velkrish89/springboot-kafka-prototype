package com.amwaydemo.consumer.model;

import com.amwaydemo.consumer.utils.OrderStatus;

import java.util.List;

public class Order {
	
	private long orderId;
	private String customerEmail;
	private String orderDate;
	private List<Product> products;
	private double price;
	private OrderStatus orderStatus;
	
	public Order() {

	}
	
	public Order(long orderId, String customerEmail, List<Product> products, double price) {
		this.orderId = orderId;
		this.customerEmail = customerEmail;
		this.products = products;
		this.price = price;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Order(long orderId, String customerEmail, String orderDate, List<Product> products, double price, OrderStatus orderStatus) {
		this.orderId = orderId;
		this.customerEmail = customerEmail;
		this.orderDate = orderDate;
		this.products = products;
		this.price = price;
		this.orderStatus = orderStatus;
	}
	
	
}
