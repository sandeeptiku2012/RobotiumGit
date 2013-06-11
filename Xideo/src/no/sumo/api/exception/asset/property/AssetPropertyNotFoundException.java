package no.sumo.api.exception.asset.property;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class AssetPropertyNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 1075478167796498783L;

	public AssetPropertyNotFoundException(Long propertyId) {
		this(propertyId, String.format("Asset property with id '%s' was not found", propertyId));
	}
	
	public AssetPropertyNotFoundException(Errorcode code, String message) {
		super(code, message);
	}

	public AssetPropertyNotFoundException(Long propertyId, String message) {
		super(Errorcode.ASSET_PROPERTY_NOT_FOUND, message, propertyId);
	}
}
