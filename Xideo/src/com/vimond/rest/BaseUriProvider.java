package com.vimond.rest;

import java.net.URI;

import com.comcast.xideo.Configuration;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class BaseUriProvider implements Provider<URI> {
	@Inject
	private Configuration configuration;

	@Override
	public URI get() {
		return configuration.getBaseUri();
	}
}
