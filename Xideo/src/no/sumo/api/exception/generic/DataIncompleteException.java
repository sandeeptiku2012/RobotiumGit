package no.sumo.api.exception.generic;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.SuperException;

public class DataIncompleteException extends SuperException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8963761205139906675L;

	public DataIncompleteException(Errorcode error, String message)
	{ 
		super(error, message);
	}
}
