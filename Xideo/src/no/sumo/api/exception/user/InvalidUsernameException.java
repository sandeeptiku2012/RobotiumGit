package no.sumo.api.exception.user;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;


public class InvalidUsernameException extends ValidationException {
	private static final long serialVersionUID = 6482820483125369953L;
	String username;

	public InvalidUsernameException(String username) {
		this(username, String.format("The username '%s' is not valid", username));
	}

	public InvalidUsernameException(String username, String message) {
		this(Errorcode.USER_INVALID_USERNAME, username, message);
		setUsername(username);
	}
	
	public InvalidUsernameException(Errorcode code, String username, String message) {
		super(code, message);
		setUsername(username);
	}
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
