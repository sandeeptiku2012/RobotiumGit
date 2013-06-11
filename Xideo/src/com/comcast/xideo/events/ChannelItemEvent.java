package com.comcast.xideo.events;

public class ChannelItemEvent implements IItemEvent {
	Object source;
	Object value;

	public ChannelItemEvent(Object source, Object value) {
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