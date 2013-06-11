package com.comcast.xideo.events;

public class VideoChangedEvent {
	private final Object source;

	public VideoChangedEvent( Object source ) {
		this.source = source;
	}

	public Object getSource() {
		return source;
	}
}
