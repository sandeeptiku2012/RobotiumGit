package com.vimond.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import com.google.inject.Inject;

@Consumes( { MediaType.APPLICATION_XML, MediaType.TEXT_XML } )
@Produces( { MediaType.APPLICATION_XML, MediaType.TEXT_XML } )
public class SimpleXml<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {
	@Inject
	private Serializer serializer;

	@Override
	public boolean isReadable( Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType ) {
		return true;
	}

	@Override
	public T readFrom( Class<T> clazz, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> paramMultivaluedMap, InputStream inputStream ) throws IOException, WebApplicationException {
		try {
			return getSerializer().read( clazz, inputStream, false );
		} catch( Exception e ) {
			e.printStackTrace();
			throw new WebApplicationException( e );
		}
	}

	private Serializer getSerializer() {
		if( serializer == null ) {
			Registry registry = new Registry();
			try {
				registry.bind( Date.class, DateConverter.class );
			} catch( Exception e ) {
				e.printStackTrace();
			}
			Strategy strategy = new RegistryStrategy( registry, new AnnotationStrategy() );
			serializer = new Persister( strategy );
		}
		return serializer;
	}

	@Override
	public long getSize( T arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4 ) {
		return 0;
	}

	@Override
	public boolean isWriteable( Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3 ) {
		return true;
	}

	@Override
	public void writeTo( T value, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> arg5, OutputStream outputStream ) throws IOException, WebApplicationException {
		try {
			getSerializer().write( value, outputStream );
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
}
