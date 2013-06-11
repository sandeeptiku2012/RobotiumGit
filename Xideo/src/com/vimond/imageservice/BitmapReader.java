package com.vimond.imageservice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@Consumes( { BitmapReader.IMAGE_PNG, BitmapReader.IMAGE_JPEG } )
public class BitmapReader implements MessageBodyReader<Bitmap> {
	public static final String IMAGE_PNG = "image/png";
	public static final String IMAGE_JPEG = "image/jpeg";
	public static final String IMAGE_ALL = "image/*";

	@Override
	public boolean isReadable( Class<?> paramClass, Type paramType, Annotation[] paramArrayOfAnnotation, MediaType paramMediaType ) {
		return true;
	}

	@Override
	public Bitmap readFrom( Class<Bitmap> paramClass, Type paramType, Annotation[] paramArrayOfAnnotation, MediaType paramMediaType, MultivaluedMap<String, String> paramMultivaluedMap, InputStream inputStream ) throws IOException, WebApplicationException {
		return BitmapFactory.decodeStream( inputStream );
	}
}
