package com.vimond.service;

public class ProductReference {
	protected long paymentId;

	public ProductReference( long paymentId ) {
		assert paymentId > 0;
		this.paymentId = paymentId;
	}

	public long getId() {
		return paymentId;
	}
}
