package com.vimond.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.sumo.api.contracts.IPlaybackItem;
import no.sumo.api.vo.asset.playback.RestPlayback;
import no.sumo.api.vo.asset.playback.RestPlaybackItem;

public class Playback {
	private RestPlayback playback;

	public Playback( RestPlayback playback ) {
		this.playback = playback;
	}

	public Date getLiveBroadcastTime() {
		return playback.getLiveBroadcastTime();
	}

	public String getTitle() {
		return playback.getTitle();
	}

	public List<PlaybackStream> getStreamURLs() {
		List<PlaybackStream> result = new ArrayList<PlaybackStream>();
		for( IPlaybackItem item : playback.getPlaybackItems() ) {
			result.add( new PlaybackStream( ((RestPlaybackItem)item).getFileId(), item.getUrl() ) );
		}
		return result;
	}
}
