package no.sumo.api.exception.asset;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;


public class AssetNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 7380429822706496945L;

	public AssetNotFoundException(Long assetId) {
		this(assetId, String.format("Asset with id '%s' was not found", assetId));
	}

	public AssetNotFoundException(Long assetId, String message) {
		super(Errorcode.ASSET_NOT_FOUND, message, assetId);
	}

	public AssetNotFoundException(Long assetId, Errorcode errorcode, String message) {
		super(errorcode, message, assetId);
	}
}
