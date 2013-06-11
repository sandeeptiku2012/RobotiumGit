package no.sumo.api.exception.asset.rating;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;

public class PrematureRatingException extends ValidationException {
	private static final long serialVersionUID = -8609826237546961134L;

	public PrematureRatingException(String message) {
		super(Errorcode.ASSET_RATING_PREMATURE, message);
	}

}
