package no.sumo.api.exception.user;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;


public class UserNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -3788931209234593506L;

	public UserNotFoundException(long id) {
		super(Errorcode.USER_NOT_FOUND, id);
	}
	
	public UserNotFoundException(long id, String message){
		super(Errorcode.USER_NOT_FOUND, message, id);
	}
	
	public UserNotFoundException(String message){
		super(Errorcode.USER_NOT_FOUND, message);
	}

}
