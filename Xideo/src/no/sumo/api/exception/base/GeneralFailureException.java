package no.sumo.api.exception.base;

import no.sumo.api.exception.Errorcode;

public class GeneralFailureException extends RuntimeException {

	private static final long serialVersionUID = -8376323072708535445L;

	private Errorcode code;

	public GeneralFailureException (Errorcode code, String message)
	{
		super (message);
		this.code = code;
	}
	
	public GeneralFailureException(Errorcode code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public Errorcode getCode() {
		return code;
	}
	public void setCode(Errorcode code) {
		this.code = code;
	}
}
