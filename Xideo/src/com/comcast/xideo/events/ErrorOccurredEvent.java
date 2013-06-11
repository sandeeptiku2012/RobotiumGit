package com.comcast.xideo.events;

public class ErrorOccurredEvent {
	private final Object source;
	private final String message;

	public ErrorOccurredEvent( Object source, String message ) {
		this.source = source;
		this.message = message;
	}

	public Object getSource() {
		return source;
	}

	public String getMessage() {
		return message;
	}
}
