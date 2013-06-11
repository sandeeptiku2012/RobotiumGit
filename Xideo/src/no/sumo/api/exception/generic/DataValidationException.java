package no.sumo.api.exception.generic;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;

public class DataValidationException extends ValidationException {
	private static final long serialVersionUID = 63246358175024385L;
	
	public DataValidationException (Errorcode error, String message){ 
		super(error, message);
	}
	
	public DataValidationException(Errorcode error, String message, Throwable cause){
		super(error, message, cause);
	}

}
