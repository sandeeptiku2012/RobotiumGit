package no.sumo.api.exception.user;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;

public class InvalidPinException extends ValidationException {
	String username;

	public InvalidPinException( String username ) {
		this( username, String.format( "The pin for username '%s' is not valid", username ) );
	}

	public InvalidPinException( String username, String message ) {
		super( Errorcode.USER_INVALID_PIN, message );
		setUsername( username );
	}

	public String getUsername() {
		return username;
	}

	public void setUsername( String username ) {
		this.username = username;
	}
}
