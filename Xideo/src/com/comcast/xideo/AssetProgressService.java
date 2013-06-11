package com.comcast.xideo;

import com.google.inject.Inject;
import com.vimond.entity.AssetProgress;
import com.vimond.entity.Video;
import com.vimond.entity.VideoReference;
import com.vimond.service.StbVideoService;

public class AssetProgressService implements AssetProgress {
	private StbVideoService videoService;

	@Inject
	public AssetProgressService( StbVideoService videoService ) {
		this.videoService = videoService;
	}

	@Override
	public void setProgress( VideoReference video, long position ) {
		videoService.setProgress( video, position );
	}

	@Override
	public Long getProgress( VideoReference video ) {
		return videoService.getProgress( video );
	}

	@Override
	public void removeProgress( VideoReference video ) {
		videoService.setProgress( video, 0L );
	}

	@Override
	public boolean hasSeen( Video video ) {
		return getProgress( video ) != null;
	}
}
