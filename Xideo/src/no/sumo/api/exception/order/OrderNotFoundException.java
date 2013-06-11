package no.sumo.api.exception.order;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class OrderNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -1144542414296154400L;

	public OrderNotFoundException(String message) {
		super(Errorcode.ORDER_NOT_FOUND, message);
	}

	
}
