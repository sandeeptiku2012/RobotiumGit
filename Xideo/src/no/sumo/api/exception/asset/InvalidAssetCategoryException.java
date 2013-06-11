package no.sumo.api.exception.asset;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;


public class InvalidAssetCategoryException extends NotFoundException {

	private static final long serialVersionUID = -3547480710101046002L;

	public InvalidAssetCategoryException(Long categoryId) {
		this(categoryId, String.format("The category with id '%s' is not a valid asset category", categoryId));
	}

	public InvalidAssetCategoryException(Long categoryId, String message) {
		super(Errorcode.ASSET_CATEGORY_INVALID, message, categoryId);
	}

}
