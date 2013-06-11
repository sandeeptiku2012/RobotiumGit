package no.sumo.api.exception;

import no.sumo.api.exception.base.ForbiddenException;


public class InvalidGeoRegionException extends ForbiddenException {
	private static final long serialVersionUID = -2783624438633736796L;

	public InvalidGeoRegionException(Long categoryId, String ipAddress) {
		this(
				Errorcode.ASSET_PLAYBACK_INVALID_GEO_LOCATION, 
				String.format("Ip address '%s' does not have access to category '%s'",  ipAddress,categoryId)
		);
	}
	
	public InvalidGeoRegionException(Errorcode error, String message) {
		super(Errorcode.ASSET_PLAYBACK_INVALID_GEO_LOCATION, message);
	}


}
