package com.comcast.videoplayer.ui;


public class SeekEventInfo implements EventInfo {
	private final long position;

	public SeekEventInfo(long position) {
		this.position = position;
	}

	public long getPosition() {
		return position;
	}
}

