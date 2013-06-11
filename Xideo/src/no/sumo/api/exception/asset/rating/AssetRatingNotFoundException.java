package no.sumo.api.exception.asset.rating;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class AssetRatingNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 4070896263959670974L;

	public AssetRatingNotFoundException(Long ratingId) {
		this(ratingId, String.format("Asset rating with id '%s' was not found", ratingId));
	}
	
	public AssetRatingNotFoundException(Errorcode code, String message) {
		super(code, message);
	}

	public AssetRatingNotFoundException(Long ratingId, String message) {
		super(Errorcode.ASSET_RATING_NOT_FOUND, message, ratingId);
	}
}
