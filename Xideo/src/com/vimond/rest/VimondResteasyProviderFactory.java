package com.vimond.rest;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class VimondResteasyProviderFactory extends ResteasyProviderFactory {
	public VimondResteasyProviderFactory() {
		setInstance( this );
	}
}
