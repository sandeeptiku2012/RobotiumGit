package no.sumo.api.exception.platform;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class PlatformNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 7720763636278887563L;
	
	private String platformIdentifier;
	
	public PlatformNotFoundException(String platformIdentifier, String message) {
		super(Errorcode.PLATFORM_NOT_FOUND, message);
		setPlatformIdentifier(platformIdentifier);
	}
	
	public PlatformNotFoundException(String message) {
		this(null, message);
	}

	public String getPlatformIdentifier() {
		return platformIdentifier;
	}

	public void setPlatformIdentifier(String platformIdentifier) {
		this.platformIdentifier = platformIdentifier;
	}

	

}
