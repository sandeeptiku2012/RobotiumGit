package no.sumo.api.exception.base;

import no.sumo.api.exception.Errorcode;

public abstract class ForbiddenException extends SuperException {
	private static final long serialVersionUID = -6719763540197284459L;

	public ForbiddenException(Errorcode error) {
		super(error);
	}
	
	public ForbiddenException(Errorcode error, Throwable cause) {
		super(error, cause);
	}	
	
	public ForbiddenException(Errorcode error, String message) {
		super(error, message);
	}
	
	public ForbiddenException(Errorcode error, String message, Throwable cause) {
		super(error, message, cause);
	}

}
