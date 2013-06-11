package com.comcast.xideo.events;

public class VideoItemEvent implements IItemEvent {
	Object source;
	Object value;

	public VideoItemEvent(Object source, Object value) {
		this.source = source;
		this.value = value;
	}

	public Object getSource() {
		return source;
	}

	public Object getValue() {
		return value;
	}
}