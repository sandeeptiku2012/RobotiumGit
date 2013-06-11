package com.comcast.xideo.events;

public class FocusItemEvent implements IItemEvent {
	Object source;
	Object value;

	public FocusItemEvent(Object source, Object value) {
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