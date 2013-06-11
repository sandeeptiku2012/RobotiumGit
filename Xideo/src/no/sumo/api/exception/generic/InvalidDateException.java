package no.sumo.api.exception.generic;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;

public class InvalidDateException extends ValidationException {
	private static final long serialVersionUID = -7163196347764062369L;

	public InvalidDateException(String message) {
		super(Errorcode.INVALID_DATE, message);
	}

}
