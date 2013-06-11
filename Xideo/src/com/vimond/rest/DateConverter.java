package com.vimond.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class DateConverter implements Converter<Date> {
	private DateFormat format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" );

	@Override
	public Date read( InputNode inputNode ) throws Exception {
		String value = inputNode.getValue();
		return read( value );
	}

	Date read( String value ) throws ParseException {
		try {
			return format.parse( value );
		} catch( ParseException e ) {
			if( value.matches( ".*([+-])(\\d\\d):(\\d\\d)$" ) ) {
				value = value.replaceFirst( "([+-])(\\d\\d):(\\d\\d)$", "$1$2$3" );
			}
			String[] patterns = new String[] {
				"yyyy-MM-dd'T'HH:mm:ss'Z'",
				"yyyy-MM-dd'T'HH:mm:ssZ",
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ",
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" };
			for( String pattern : patterns ) {
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat( pattern );
					return simpleDateFormat.parse( value );
				} catch( ParseException ex ) {
				}
			}
			throw e;
		}
	}

	@Override
	public void write( OutputNode outputNode, Date date ) throws Exception {
		outputNode.setValue( format.format( date ) );
	}
}
