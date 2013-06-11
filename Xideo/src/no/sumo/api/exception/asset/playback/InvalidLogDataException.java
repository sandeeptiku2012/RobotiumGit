package no.sumo.api.exception.asset.playback;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;



public class InvalidLogDataException extends ValidationException {
	private static final long serialVersionUID = -6043808026504375362L;

	public InvalidLogDataException(Errorcode error, String message) {
		super(error, message);
	}
	
	public InvalidLogDataException(Errorcode error, String message, Throwable cause) {
		super(error, message, cause);
	}


}
