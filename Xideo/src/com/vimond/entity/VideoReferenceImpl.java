package com.vimond.entity;


public class VideoReferenceImpl implements VideoReference {
	private final long id;

	public VideoReferenceImpl( long id ) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}
}