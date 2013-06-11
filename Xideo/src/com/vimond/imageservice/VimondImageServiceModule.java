package com.vimond.imageservice;

import org.jboss.resteasy.client.ProxyFactory;

import com.comcast.xideo.Constants;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.vimond.rest.AndroidClientExecutor;
import com.vimond.rest.VimondResteasyProviderFactory;

public class VimondImageServiceModule extends AbstractModule {
	@Override
	protected void configure() {
	}

	@Provides
	IVimondImageService createVimondImageService() {
		VimondResteasyProviderFactory factory = new VimondResteasyProviderFactory();
		factory.addMessageBodyReader( new BitmapReader() );
		return ProxyFactory.create( IVimondImageService.class, ProxyFactory.createUri( Constants.VIMOND_IMAGE_SERVICE_BASE_URI ), new AndroidClientExecutor(), factory );
	}
}
