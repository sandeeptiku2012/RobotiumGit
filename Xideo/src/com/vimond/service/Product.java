package com.vimond.service;


public class Product extends ProductReference {
	private Double price;

	public Product( long paymentId, Double price ) {
		super( paymentId );
		this.price = price > 0 ? price : null;
	}

	public Double getPrice() {
		return price;
	}

	public boolean isFree() {
		return price == null;
	}
}
