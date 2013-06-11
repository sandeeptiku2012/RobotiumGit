package com.vimond.entity;


public class ProductGroupReference implements Reference {
	private long id;

	public ProductGroupReference( long id ) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return (int)getId();
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) {
			return true;
		}
		if( !(o instanceof ProductGroupReference) ) {
			return false;
		}
		return getId() == ((ProductGroupReference)o).getId();
	}
}
