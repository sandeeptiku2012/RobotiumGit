package no.sumo.api.exception.user;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;


public class InvalidPasswordException extends ValidationException {
	private static final long serialVersionUID = -862403745975679340L;
	String username;
	
	public InvalidPasswordException(String username) {
		this(username, String.format("The password for username '%s' is not valid", username));
	}

	public InvalidPasswordException(String username, String message) {
		super(Errorcode.USER_INVALID_PASSWORD, message);
		setUsername(username);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
