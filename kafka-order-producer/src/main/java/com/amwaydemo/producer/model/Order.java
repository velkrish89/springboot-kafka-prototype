package com.amwaydemo.producer.model;

import com.amwaydemo.producer.utils.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="Customer_Order")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(hidden = true)
	private long orderId;
	private String customerEmail;
	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	private String orderDate;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_order_product", referencedColumnName = "orderId")
	private List<OrderProduct> products;
	private double price;
	@Enumerated(EnumType.STRING)
	@Schema(hidden = true)
	private OrderStatus orderStatus;
	
	
	public Order() {
		this.orderStatus = OrderStatus.CREATED;
	}
	
	public Order(long orderId, String customerEmail, List<OrderProduct> products, double price) {
		this.orderId = orderId;
		this.customerEmail = customerEmail;
		this.products = products;
		this.price = price;
		this.orderStatus = OrderStatus.CREATED;
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

	public List<OrderProduct> getProducts() {
		return products;
	}

	public void setProducts(List<OrderProduct> products) {
		this.products = products;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
}
