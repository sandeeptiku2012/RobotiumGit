package no.sumo.api.exception;

import no.sumo.api.exception.base.NotFoundException;



public class ThirdPartyErrorException extends NotFoundException {

	private static final long serialVersionUID = 7380429822706496945L;

	
	public ThirdPartyErrorException(String message) {
		super(Errorcode.THIRDPARTY_ERROR, message);
	}
	
	public ThirdPartyErrorException(String message, Throwable cause) {
		super(Errorcode.THIRDPARTY_ERROR, message, cause);
	}
	
}
