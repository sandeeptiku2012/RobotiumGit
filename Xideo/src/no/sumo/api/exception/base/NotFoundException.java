package no.sumo.api.exception.base;

import no.sumo.api.exception.Errorcode;

/**
 * Provides a base class for creating exceptions in the "NotFound" family
 */
public abstract class NotFoundException extends SuperException {
	private static final long serialVersionUID = 3854344401761570956L;

	Long id;
	
	public NotFoundException(Errorcode error) {
		super(error);
	}
	
	public NotFoundException(Errorcode error, Throwable cause) {
		super(error, cause);
	}	
	
	
	
	public NotFoundException(Errorcode error, String message) {
		super(error, message);
	}
	
	public NotFoundException(Errorcode error, String message, Throwable cause) {
		super(error, message, cause);
	}
	
	
	
	public NotFoundException(Errorcode error, Long id) {
		this(error);
		setId(id);
	}
	
	public NotFoundException(Errorcode error, String message, Long id) {
		this(error, message);
		setId(id);
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
