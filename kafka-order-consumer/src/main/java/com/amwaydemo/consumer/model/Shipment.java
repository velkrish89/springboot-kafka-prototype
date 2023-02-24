package com.amwaydemo.consumer.model;

import com.amwaydemo.consumer.utils.OrderStatus;
import com.amwaydemo.consumer.utils.ShippingStatus;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Shipment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long shipmentId;
	private long orderId;
	private String customerEmail;
	private String orderDate;
	
	@OneToMany(targetEntity = Product.class, cascade = CascadeType.ALL)
	@JoinColumn(name="fk_ship_product", referencedColumnName = "shipmentId")
	private List<Product> products;
	private double price;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@Enumerated(EnumType.STRING)
	private ShippingStatus shippingStatus;
	private String lastUpdated;
	
	public long getShipmentId() {
		return shipmentId;
	}
	
	public void setShipmentId(long shipmentId) {
		this.shipmentId = shipmentId;
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
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}
	
	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}
	
	public String getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Shipment(long shipmentId, long orderId, String customerEmail, String orderDate, List<Product> products, double price,
					OrderStatus orderStatus, ShippingStatus shippingStatus, String lastUpdated) {
		super();
		this.shipmentId = shipmentId;
		this.orderId = orderId;
		this.customerEmail = customerEmail;
		this.orderDate = orderDate;
		this.products = products;
		this.price = price;
		this.orderStatus = orderStatus;
		this.shippingStatus = shippingStatus;
		this.lastUpdated = lastUpdated;
	}
	
	public Shipment() {
		
	}
	
	
	

}
