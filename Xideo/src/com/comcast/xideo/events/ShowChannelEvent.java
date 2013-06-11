package com.comcast.xideo.events;

import com.vimond.entity.ChannelReference;

public class ShowChannelEvent {
	private final Object source;
	private final ChannelReference channel;

	public ShowChannelEvent( Object source, ChannelReference channel ) {
		this.source = source;
		this.channel = channel;
	}

	public Object getSource() {
		return source;
	}

	public ChannelReference getChannel() {
		return channel;
	}
}
