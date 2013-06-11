package com.goldengate.guice;

public class DataReadyEvent {
	private Object source;

	public DataReadyEvent( Object source ) {
		this.source = source;
	}

	public Object getSource() {
		return source;
	}
}
