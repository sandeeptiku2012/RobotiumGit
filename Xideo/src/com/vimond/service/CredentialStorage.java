package com.vimond.service;

public interface CredentialStorage {
	public void save( String username, String password ) throws Exception;

	public Credentials load() throws Exception;

	public void clear();
}
