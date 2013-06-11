package no.sumo.api.exception.user;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;


public class InvalidEmailException extends ValidationException {
	private static final long serialVersionUID = 6482820483125369953L;
	final String email;

	public InvalidEmailException(String email) {
		this(email, String.format("The email '%s' is not valid", email));
	}

	public InvalidEmailException(String email, String message) {
		super(Errorcode.USER_INVALID_EMAIL, message);
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
