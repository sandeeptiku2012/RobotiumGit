package com.vimond.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse.BaseClientResponseStreamFactory;
import org.jboss.resteasy.client.core.SelfExpandingBufferredInputStream;
import org.jboss.resteasy.util.CaseInsensitiveMap;

import roboguice.util.Ln;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class AndroidClientExecutor implements ClientExecutor {
	@Inject
	private ExceptionMapper exceptionMapper;

	protected HttpClient httpClient;
	protected boolean createdHttpClient;
	protected HttpContext httpContext;
	protected boolean closed;

	public AndroidClientExecutor() {
		httpClient = new DefaultHttpClient();
		createdHttpClient = true;
	}

	public AndroidClientExecutor( HttpClient httpClient ) {
		this.httpClient = httpClient;
	}

	@Inject
	public AndroidClientExecutor( HttpClient httpClient, HttpContext httpContext ) {
		this.httpClient = httpClient;
		this.httpContext = httpContext;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public HttpContext getHttpContext() {
		return httpContext;
	}

	public void setHttpContext( HttpContext httpContext ) {
		this.httpContext = httpContext;
	}

	public static CaseInsensitiveMap<String> extractHeaders( HttpResponse response ) {
		final CaseInsensitiveMap<String> headers = new CaseInsensitiveMap<String>();

		for( Header header : response.getAllHeaders() ) {
			headers.add( header.getName(), header.getValue() );
		}
		return headers;
	}

	@Override
	public ClientRequest createRequest( String uriTemplate ) {
		return new ClientRequest( uriTemplate, this );
	}

	@Override
	public ClientRequest createRequest( UriBuilder uriBuilder ) {
		return new ClientRequest( uriBuilder, this );
	}

	@Override
	public ClientResponse<?> execute( ClientRequest request ) throws Exception {
		final String uri = request.getUri();
		final HttpRequestBase httpMethod = createHttpMethod( uri, request.getHttpMethod() );
		httpMethod.setHeader( "Accept-Encoding", "gzip" );
		loadHttpMethod( request, httpMethod );

		Ln.i( "Connecting (%s) to '%s'", request.getHttpMethod(), uri );
		BaseClientResponse<HttpEntity> response = null;
		HttpResponse res = null;
		try {
			res = httpClient.execute( httpMethod, httpContext );
		} catch( IOException ex ) {
			Ln.e( ex, "HHH: %s (%s)", ex, uri );
			throw ex;
		}
		final HttpResponse res2 = res;
		response = new BaseClientResponse<HttpEntity>( new BaseClientResponseStreamFactory() {
			InputStream stream;

			@Override
			public InputStream getInputStream() throws IOException {
				if( stream == null ) {
					HttpEntity entity = res2.getEntity();
					if( entity == null ) {
						return null;
					}
					String encoding = entity.getContentEncoding().getValue();
					Ln.i( "Received: %s (%s)", uri, encoding );

					InputStream content = entity.getContent();
					if( "gzip".equals( encoding ) ) {
						content = new GZIPInputStream( content );
					}
					stream = new SelfExpandingBufferredInputStream( content );
				}
				return stream;
			}

			@Override
			public void performReleaseConnection() {
				// Apache Client 4 is stupid,  You have to get the InputStream and close it if there is an entity
				// otherwise the connection is never released.  There is, of course, no close() method on response
				// to make this easier.
				try {
					if( stream != null ) {
						stream.close();
					} else {
						InputStream is = getInputStream();
						if( is != null ) {
							is.close();
						}
					}
				} catch( Exception ignore ) {
				}
			}
		}, this ) {
			@Override
			public void checkFailureStatus() {
				if( status <= 399 || status >= 599 ) {
					return;
				}
				throw createResponseFailure( String.format( "Error status %d %s returned", new Object[] { Integer.valueOf( status ), getResponseStatus() } ), readException() );
			}

			private Exception readException() {
				GenericException ex = getEntity( GenericException.class );
				if( exceptionMapper != null ) {
					return exceptionMapper.convert( ex );
				}
				return ex;
			}
		};
		response.setAttributes( request.getAttributes() );
		response.setStatus( res.getStatusLine().getStatusCode() );
		response.setHeaders( extractHeaders( res ) );
		response.setProviderFactory( request.getProviderFactory() );
		return response;
	}

	private HttpRequestBase createHttpMethod( String url, String restVerb ) {
		if( "GET".equals( restVerb ) ) {
			//url = Uri.decode( url );
			return new HttpGet( url );
		} else if( "POST".equals( restVerb ) ) {
			return new HttpPost( url );
		} else {
			final String verb = restVerb;
			return new HttpPost( url ) {
				@Override
				public String getMethod() {
					return verb;
				}
			};
		}
	}

	public void loadHttpMethod( final ClientRequest request, HttpRequestBase httpMethod ) throws Exception {
		if( httpMethod instanceof HttpGet && request.followRedirects() ) {
			HttpClientParams.setRedirecting( httpMethod.getParams(), true );
		} else {
			HttpClientParams.setRedirecting( httpMethod.getParams(), false );
		}

		if( request.getBody() != null && !request.getFormParameters().isEmpty() ) {
			throw new RuntimeException( "You cannot send both form parameters and an entity body" );
		}

		if( !request.getFormParameters().isEmpty() ) {
			commitHeaders( request, httpMethod );
			HttpPost post = (HttpPost)httpMethod;

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();

			for( Map.Entry<String, List<String>> formParam : request.getFormParameters().entrySet() ) {
				List<String> values = formParam.getValue();
				for( String value : values ) {
					formparams.add( new BasicNameValuePair( formParam.getKey(), value ) );
				}
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity( formparams, "UTF-8" );
			post.setEntity( entity );
		} else if( request.getBody() != null ) {
			if( httpMethod instanceof HttpGet ) {
				throw new RuntimeException( "A GET request cannot have a body." );
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				request.writeRequestBody( request.getHeadersAsObjects(), baos );
				ByteArrayEntity entity = new ByteArrayEntity( baos.toByteArray() ) {
					@Override
					public Header getContentType() {
						return new BasicHeader( "Content-Type", request.getBodyContentType().toString() );
					}
				};
				HttpPost post = (HttpPost)httpMethod;
				commitHeaders( request, httpMethod );
				post.setEntity( entity );
			} catch( IOException e ) {
				throw new RuntimeException( e );
			}
		} else // no body
		{
			commitHeaders( request, httpMethod );
		}
	}

	public void commitHeaders( ClientRequest request, HttpRequestBase httpMethod ) {
		MultivaluedMap<String, String> headers = request.getHeaders();
		for( Map.Entry<String, List<String>> header : headers.entrySet() ) {
			List<String> values = header.getValue();
			for( String value : values ) {
				//               System.out.println(String.format("setting %s = %s", header.getKey(), value));
				httpMethod.addHeader( header.getKey(), value );
			}
		}
	}

	@Override
	public void close() {
		if( closed ) {
			return;
		}

		if( createdHttpClient && httpClient != null ) {
			ClientConnectionManager manager = httpClient.getConnectionManager();
			if( manager != null ) {
				manager.shutdown();
			}
		}
		closed = true;
	}

	public boolean isClosed() {
		return closed;
	}

	@Override
	public void finalize() throws Throwable {
		close();
		super.finalize();
	}
}