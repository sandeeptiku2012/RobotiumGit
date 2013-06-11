package com.vimond.rest;

import no.sumo.api.exception.payment.PaymentException;
import no.sumo.api.exception.product.ProductPaymentNotFoundException;
import no.sumo.api.exception.user.InvalidPinException;

public class ExceptionMapper {
	public Exception convert( GenericException exception ) {
		switch( exception.code ) {
			case USER_INVALID_PIN:
				return new InvalidPinException( null, exception.description );
			case PAYMENT_ERROR:
				return new PaymentException( exception.description, exception.endUserMessage );
			case PRODUCT_PAYMENT_NOT_FOUND:
				return new ProductPaymentNotFoundException( exception.description );
			default:
				break;
		}
		return exception;
	}
}
