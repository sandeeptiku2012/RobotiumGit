package no.sumo.api.exception.asset.playback;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.SuperException;

public class UnsupportedVideoFormatException extends SuperException {

	private static final long serialVersionUID = -7837376878836258456L;

	public UnsupportedVideoFormatException(String message) {
		
		super(Errorcode.ASSET_PLAYBACK_INVALID_VIDEO_FORMAT, message);
	}

}
