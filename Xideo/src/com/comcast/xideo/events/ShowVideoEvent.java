package com.comcast.xideo.events;

import java.util.List;

import com.vimond.entity.VideoReference;

public class ShowVideoEvent {
	private final Object source;
	private final List<VideoReference> videos;

	public ShowVideoEvent( Object source, List<VideoReference> list ) {
		this.source = source;
		videos = list;
	}

	public Object getSource() {
		return source;
	}

	public List<VideoReference> getVideos() {
		return videos;
	}
}
