package com.comcast.xideo;

public class ItemSelectedEvent {
	Object source;
	Object value;

	public ItemSelectedEvent(Object source, Object view) {
		this.source = source;
		this.value = view;
	}

	public Object getSource() {
		return source;
	}

	public Object getValue() {
		return value;
	}
}

