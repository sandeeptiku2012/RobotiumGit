package com.vimond.entity;

public class ChannelReferenceImpl implements ChannelReference {
	private long id;

	public ChannelReferenceImpl( long id ) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}
}