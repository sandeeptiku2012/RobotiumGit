package no.sumo.api.exception.base;

import no.sumo.api.exception.Errorcode;

public abstract class ValidationException extends SuperException {
	private static final long serialVersionUID = 660635529150456592L;

	public ValidationException(Errorcode error){
		super(error);
	}
	
	public ValidationException(Errorcode error, Throwable cause){
		super(error, cause);
	}
	
	public ValidationException(Errorcode error, String message){
		super(error, message);
	}
	
	public ValidationException(Errorcode error, String message, Throwable cause){
		super(error, message, cause);
	}
}
