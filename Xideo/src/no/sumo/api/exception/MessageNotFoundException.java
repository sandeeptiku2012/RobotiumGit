package no.sumo.api.exception;


public class MessageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4791048032596512725L;

	String key;
	
	public MessageNotFoundException(String key) {
		this.key = key;
	}

}
