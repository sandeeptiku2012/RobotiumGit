package no.sumo.api.exception.product;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class UnsupportedPaymentException extends NotFoundException {
	private static final long serialVersionUID = 2804512777335578162L;

	public UnsupportedPaymentException(Errorcode error, String message) {
		super(error, message);
	}

}
