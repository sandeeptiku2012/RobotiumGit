package com.comcast.xideo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import android.os.Environment;

import com.google.inject.Singleton;

@Singleton
public class Configuration {
	private Properties properties;

	public URI getBaseUri() {
		try {
			return new URI( getProperty( "baseuri", Constants.VIMOND_API_BASEURI ) );
		} catch( URISyntaxException e ) {
			throw new RuntimeException( e );
		}
	}

	private String getProperty( String key, String defaultValue ) {
		return getProperties().getProperty( key, defaultValue );
	}

	private Properties getProperties() {
		if( properties == null ) {
			properties = new Properties();
			try {
				File file = new File( Environment.getExternalStorageDirectory(), Constants.PROPERTIES_FILENAME );
				properties.load( new FileInputStream( file ) );
			} catch( IOException e ) {
			}
		}
		return properties;
	}
}
