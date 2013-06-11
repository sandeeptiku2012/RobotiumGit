package com.comcast.xideo.videoplayer;

import java.util.List;

import com.vimond.entity.Video;
import com.vimond.entity.VideoReference;
import com.vimond.entity.VideoReferenceImpl;

public class VideoQueue {
	private int currentAssetNumPlaying = 0;
	private long[] assetIds;

	public VideoQueue( long[] assetId ) {
		assetIds = assetId;
	}

	public VideoReference getCurrentVideo() {
		return new VideoReferenceImpl( assetIds[ currentAssetNumPlaying ] );
	}

	public void setPlaylist( List<Video> videos ) {
		assetIds = new long[ videos.size() ];

		int index = 0;
		for( VideoReference video : videos ) {
			assetIds[ index++ ] = video.getId();
		}

		currentAssetNumPlaying = 0;
	}

	public boolean hasNext() {
		return currentAssetNumPlaying + 1 < assetIds.length;
	}

	public VideoReference getNextVideo() {
		return hasNext() ? new VideoReferenceImpl( assetIds[ currentAssetNumPlaying + 1 ] ) : null;
	}

	public void moveToNext() {
		currentAssetNumPlaying++;
	}
}
