package no.sumo.api.exception.asset;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class AssetNotAvailableForPlaybackException extends NotFoundException {
	private static final long serialVersionUID = 3089235446687954140L;

	public AssetNotAvailableForPlaybackException(Long assetId, Integer platformId) {
		super(
			Errorcode.ASSET_PLAYBACK_INVALID_VIDEO_FORMAT, 
			String.format("asset with id '%s' is not available for playback on platform '%s'", assetId, platformId)
		);
	}

}
