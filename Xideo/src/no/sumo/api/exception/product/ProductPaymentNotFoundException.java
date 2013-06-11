package no.sumo.api.exception.product;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class ProductPaymentNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -5214357191798640104L;

	public ProductPaymentNotFoundException(String message) {
		super(Errorcode.PRODUCT_PAYMENT_NOT_FOUND, message);
	}

	
	public ProductPaymentNotFoundException(String message, Throwable cause) {
		super(Errorcode.PRODUCT_PAYMENT_NOT_FOUND, message, cause);
	}

}
