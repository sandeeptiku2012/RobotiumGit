package no.sumo.api.exception.product;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -7055481589227950278L;

	public ProductNotFoundException(String message) {
		super(Errorcode.PRODUCT_NOT_FOUND, message);
	}

}
