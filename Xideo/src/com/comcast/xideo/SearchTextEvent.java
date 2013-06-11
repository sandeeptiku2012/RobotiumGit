package com.comcast.xideo;


public class SearchTextEvent {
	Object source;
	String text;

	public SearchTextEvent(Object source, String text) {
		this.source = source;
		this.text = text;
	}

	public Object getSource() {
		return source;
	}

	public String getText() {
		return text;
	}
}

