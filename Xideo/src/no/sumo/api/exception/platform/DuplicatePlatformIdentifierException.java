package no.sumo.api.exception.platform;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;

public class DuplicatePlatformIdentifierException extends ValidationException {
	private static final long serialVersionUID = -7509294364537234453L;

	public DuplicatePlatformIdentifierException(String message) {
		super(Errorcode.DUPLICATE_PLATFORM_IDENTIFIER, message);
	}

}
