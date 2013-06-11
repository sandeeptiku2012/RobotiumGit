package no.sumo.api.exception.product;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class ProductGroupNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -8448589486820597423L;

	public ProductGroupNotFoundException(String message) {
		super(Errorcode.PRODUCT_GROUP_NOT_FOUND, message);
	}

}
