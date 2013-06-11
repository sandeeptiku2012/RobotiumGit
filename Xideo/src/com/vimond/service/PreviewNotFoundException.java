package com.vimond.service;

public class PreviewNotFoundException extends Exception {
	private static final long serialVersionUID = -9166002281491752797L;

	public PreviewNotFoundException( Exception ex ) {
		super( ex );
	}
}
