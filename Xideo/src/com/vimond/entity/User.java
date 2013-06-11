package com.vimond.entity;

public class User implements UserReference {
	protected long id;

	public User( long id ) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

}
