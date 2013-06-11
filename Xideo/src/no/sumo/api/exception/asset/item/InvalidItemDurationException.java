package no.sumo.api.exception.asset.item;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.generic.DataValidationException;

public class InvalidItemDurationException extends DataValidationException {
	private static final long serialVersionUID = 3111571615774222523L;

	public InvalidItemDurationException(String message) {
		super(Errorcode.ASSET_ITEM_INVALID_DURATION, message);
	}

}
