package no.sumo.api.exception.asset.item;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class AssetItemNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 7380429822706496945L;

	public AssetItemNotFoundException(Long itemId) {
		this(itemId, String.format("Asset item with id '%s' was not found", itemId));
	}
	
	public AssetItemNotFoundException(Errorcode code, String message) {
		super(code, message);
	}

	public AssetItemNotFoundException(Long itemId, String message) {
		super(Errorcode.ASSET_ITEM_NOT_FOUND, message, itemId);
	}

}
