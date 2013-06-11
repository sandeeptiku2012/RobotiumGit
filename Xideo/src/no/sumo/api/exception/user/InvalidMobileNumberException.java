package no.sumo.api.exception.user;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;

public class InvalidMobileNumberException extends ValidationException {
	private static final long serialVersionUID = 7912657603332895794L;

	public InvalidMobileNumberException(String message) {
		super(Errorcode.USER_INVALID_MOBILENUMBER, message);
	}
	
}
