package com.comcast.xideo;

import android.graphics.Point;

public class FocusChangeEvent {
	Object source;
	Object value;
	Point[] points;

	public FocusChangeEvent(Object source, Object view, Point[] points) {
		this.source = source;
		this.value = view;
		this.points = points;
	}

	public Object getSource() {
		return source;
	}

	public Object getValue() {
		return value;
	}

	public Point[] getPoints() {
		return points;
	}
}

