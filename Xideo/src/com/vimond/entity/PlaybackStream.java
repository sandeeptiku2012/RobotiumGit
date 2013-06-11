package com.vimond.entity;

public class PlaybackStream {
	private final long id;
	private final String url;

	public PlaybackStream( long id, String url ) {
		this.id = id;
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}
}
