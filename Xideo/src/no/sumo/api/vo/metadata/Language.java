package no.sumo.api.vo.metadata;

import java.security.InvalidParameterException;
import java.util.Locale;

public class Language {
	private final Locale locale;

	private Language( Locale locale ) {
		if( locale == null ) {
			throw new InvalidParameterException( "locale cannot be null" );
		}
		this.locale = locale;
	}

	private Language( String token ) {
		this( new Locale( token ) );
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public String toString() {
		return getLocale().toString();
	}

	public static Language valueOf( String language ) {
		Locale locale = new Locale( language );
		return new Language( locale );
	}
}