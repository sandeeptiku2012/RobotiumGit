package com.vimond.rest;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.ws.rs.ext.RuntimeDelegate;

import no.sumo.api.service.IAssetRelationsService;
import no.sumo.api.service.IAssetService;
import no.sumo.api.service.IAuthenticationService;
import no.sumo.api.service.IBaseService;
import no.sumo.api.service.ICategoriesService;
import no.sumo.api.service.ICategoryService;
import no.sumo.api.service.IChannelsService;
import no.sumo.api.service.IContentPanelService;
import no.sumo.api.service.IFavoriteService;
import no.sumo.api.service.IFindUserService;
import no.sumo.api.service.ILiveCenterService;
import no.sumo.api.service.IMessageService;
import no.sumo.api.service.IMetadataService;
import no.sumo.api.service.IOrderService;
import no.sumo.api.service.IPaymentProviderService;
import no.sumo.api.service.IPlayqueueService;
import no.sumo.api.service.IProductService;
import no.sumo.api.service.ISearchService;
import no.sumo.api.service.ISubtitleService;
import no.sumo.api.service.IUserContentService;
import no.sumo.api.service.IUserPropertyValueLoginService;
import no.sumo.api.service.IUserService;
import no.sumo.api.service.IVoucherService;
import no.sumo.api.vo.platform.RestPlatform;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.core.extractors.DefaultEntityExtractorFactory;
import org.jboss.resteasy.client.core.extractors.EntityExtractorFactory;
import org.jboss.resteasy.plugins.providers.StringTextStar;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import com.comcast.xideo.Constants;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

public class VimondApiModule extends AbstractModule {
	@Override
	protected void configure() {
		bind( CookieStore.class ).to( BasicCookieStore.class ).in( Scopes.SINGLETON );
		bind( RestPlatform.class ).toInstance( new RestPlatform( 0, Constants.VIMOND_API_PLATFORM ) );
		bind( ClientExecutor.class ).to( AndroidClientExecutor.class );
		bind( EntityExtractorFactory.class ).to( DefaultEntityExtractorFactory.class );
	}

	@Provides
	HttpClient getThreadSafeClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();
		return new DefaultHttpClient( new ThreadSafeClientConnManager( params, mgr.getSchemeRegistry() ), params );
	}

	@Provides
	@Singleton
	Executor createExecutor() {
		return Executors.newFixedThreadPool( 10 );
	}

	@Provides
	HttpContext getHttpContext( CookieStore cookieStore ) {
		HttpContext context = new BasicHttpContext();
		context.setAttribute( ClientContext.COOKIE_STORE, cookieStore );
		return context;
	}

	@Provides
	@Singleton
	ResteasyProviderFactory getResteasyProviderFactory( SimpleXml<?> xml ) {
		ResteasyProviderFactory instance = new VimondResteasyProviderFactory();
		instance.registerProviderInstance( xml );
		instance.registerProvider( StringTextStar.class );
		instance.registerProvider( ProgramSortByConverter.class );
		RuntimeDelegate.setInstance( instance );
		return instance;
	}

	@Provides
	public IAssetRelationsService getAssetRelationsService( ServiceProxyFactory factory ) {
		return factory.create( IAssetRelationsService.class );
	}

	@Provides
	public IAssetService getAssetService( ServiceProxyFactory factory ) {
		return factory.create( IAssetService.class );
	}

	@Provides
	public IAuthenticationService getAuthenticationService( ServiceProxyFactory factory ) {
		return factory.create( IAuthenticationService.class );
	}

	@Provides
	public IBaseService getBaseService( ServiceProxyFactory factory ) {
		return factory.create( IBaseService.class );
	}

	@Provides
	public IAutoCompleteService getAutoCompleteService( ServiceProxyFactory factory ) {
		return factory.create( IAutoCompleteService.class );
	}

	@Provides
	public ICategoriesService getCategoriesService( ServiceProxyFactory factory ) {
		return factory.create( ICategoriesService.class );
	}

	@Provides
	public ICategoryService getCategoryService( ServiceProxyFactory factory ) {
		return factory.create( ICategoryService.class );
	}

	@Provides
	public IChannelsService getChannelsService( ServiceProxyFactory factory ) {
		return factory.create( IChannelsService.class );
	}

	@Provides
	public IContentPanelService getContentPanelService( ServiceProxyFactory factory ) {
		return factory.create( IContentPanelService.class );
	}

	@Provides
	public IFavoriteService getFavoriteService( ServiceProxyFactory factory ) {
		return factory.create( IFavoriteService.class );
	}

	@Provides
	public IFindUserService getFindUserService( ServiceProxyFactory factory ) {
		return factory.create( IFindUserService.class );
	}

	@Provides
	public ILiveCenterService getLiveCenterService( ServiceProxyFactory factory ) {
		return factory.create( ILiveCenterService.class );
	}

	@Provides
	public IMessageService getMessageService( ServiceProxyFactory factory ) {
		return factory.create( IMessageService.class );
	}

	@Provides
	public IMetadataService getMetadataService( ServiceProxyFactory factory ) {
		return factory.create( IMetadataService.class );
	}

	@Provides
	public IOrderService getOrderService( ServiceProxyFactory factory ) {
		return factory.create( IOrderService.class );
	}

	@Provides
	public IPaymentProviderService getPaymentProviderService( ServiceProxyFactory factory ) {
		return factory.create( IPaymentProviderService.class );
	}

	@Provides
	public IPlayqueueService getPlayqueueService( ServiceProxyFactory factory ) {
		return factory.create( IPlayqueueService.class );
	}

	@Provides
	public IProductService getProductService( ServiceProxyFactory factory ) {
		return factory.create( IProductService.class );
	}

	@Provides
	public ISearchService getSearchService( ServiceProxyFactory factory ) {
		return factory.create( ISearchService.class );
	}

	@Provides
	public ISubtitleService getSubtitleService( ServiceProxyFactory factory ) {
		return factory.create( ISubtitleService.class );
	}

	@Provides
	public IUserContentService getUserContentService( ServiceProxyFactory factory ) {
		return factory.create( IUserContentService.class );
	}

	@Provides
	public IUserPropertyValueLoginService getUserPropertyValueLoginService( ServiceProxyFactory factory ) {
		return factory.create( IUserPropertyValueLoginService.class );
	}

	@Provides
	public IUserService getUserService( ServiceProxyFactory factory ) {
		return factory.create( IUserService.class );
	}

	@Provides
	public IVoucherService getVoucherService( ServiceProxyFactory factory ) {
		return factory.create( IVoucherService.class );
	}

	@Provides
	public Serializer getSerializer() {
		Registry registry = new Registry();
		try {
			registry.bind( Date.class, DateConverter.class );
		} catch( Exception e ) {
			e.printStackTrace();
		}
		Strategy strategy = new RegistryStrategy( registry, new AnnotationStrategy() );
		return new Persister( strategy );
	}
}
