package no.sumo.api.exception.asset;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;


public class AssetsNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 8051038559010976442L;

	public AssetsNotFoundException(Long categoryId) {
		this(categoryId, String.format("Assets in categoryId '%s' was not found", categoryId));
	}

	public AssetsNotFoundException(Long categoryId, String message) {
		super(Errorcode.ASSET_CATEGORY_INVALID, message, categoryId);
	}

}
