package com.vimond.rest;

import java.util.Map;

import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.extractors.EntityExtractorFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
class ServiceProxyFactory {
	static {
	}

	@Inject
	private BaseUriProvider baseUriProvider;

	@Inject
	private ClientExecutor executor;

	@Inject
	private ResteasyProviderFactory providerFactory;

	@Inject
	private EntityExtractorFactory extractorFactory;

	private Map<String, Object> requestAttributes;

	public <T> T create( Class<T> clazz ) {
		return ProxyFactory.create( clazz, baseUriProvider.get(), executor, providerFactory, extractorFactory, requestAttributes );
	}
}