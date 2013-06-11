package no.sumo.api.exception.user;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;

public class InvalidCredentialsException extends ValidationException {

	private static final long serialVersionUID = -5954984776883940398L;

	public InvalidCredentialsException(String message) 
	{
		super(Errorcode.USER_INVALID_CREDENTIALS, message);
	}

	public InvalidCredentialsException(String message, Throwable cause) {
		super(Errorcode.USER_INVALID_CREDENTIALS, message, cause);
	}
}
