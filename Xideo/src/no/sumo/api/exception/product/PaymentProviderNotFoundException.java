package no.sumo.api.exception.product;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class PaymentProviderNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -8448834598020597423L;

	public PaymentProviderNotFoundException(String message) {
		super(Errorcode.PAYMENT_PROVIDER_NOT_FOUND, message);
	}

}
