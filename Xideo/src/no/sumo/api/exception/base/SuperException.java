package no.sumo.api.exception.base;

import no.sumo.api.exception.Errorcode;

public abstract class SuperException extends Exception {
	private static final long serialVersionUID = 660635529150456592L;
	private Errorcode code;

	public SuperException(Errorcode error){
		super();
		setErrorCode(error);
	}
	
	public SuperException(Errorcode error, String message){
		super(message);
		setErrorCode(error);
	}
	
	public SuperException(Errorcode error, Throwable cause){
		super(cause);
		setErrorCode(error);
	}
	
	public SuperException(Errorcode error, String message, Throwable cause){
		super(message, cause);
		setErrorCode(error);
	}
	
	
	public Errorcode getErrorCode() {
		return code;
	}

	public void setErrorCode(Errorcode code) {
		this.code = code;
	}
}
