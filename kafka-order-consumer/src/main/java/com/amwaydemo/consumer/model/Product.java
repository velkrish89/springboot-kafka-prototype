package com.amwaydemo.consumer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Product {
	
	@Id
	private long id;
	private long sku;
	private String productName;
	private int quantity;
	
	public Product() {
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSku() {
		return sku;
	}

	public void setSku(long sku) {
		this.sku = sku;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product(long sku, String productName, int quantity) {
		this.sku = sku;
		this.productName = productName;
		this.quantity = quantity;
	}
	
	

}
