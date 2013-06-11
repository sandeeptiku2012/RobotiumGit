package com.comcast.xideo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import roboguice.inject.ExtraConverter;
import roboguice.util.Ln;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;

import com.goldengate.guice.BackgroundThread;
import com.goldengate.guice.CategoryExtraConverter;
import com.goldengate.guice.PublisherExtraConverter;
import com.goldengate.guice.UiThread;
import com.goldengate.ui.CurrencyFormat;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.vimond.entity.AssetProgress;
import com.vimond.entity.CategoryReference;
import com.vimond.entity.Publisher;
import com.vimond.service.CredentialStorage;
import com.vimond.service.SharedPreferencesCredentialStorage;

public class XideoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind( AssetProgress.class ).to( AssetProgressService.class );
		bind( Locale.class ).toInstance( Locale.US );
		bind( new TypeLiteral<ExtraConverter<String, Publisher>>() {} ).to( PublisherExtraConverter.class );
		bind( new TypeLiteral<ExtraConverter<Long, CategoryReference>>() {} ).to( CategoryExtraConverter.class );
		bind( CredentialStorage.class ).to( SharedPreferencesCredentialStorage.class );
	}

	@Provides
	@Named( "normal" )
	@Singleton
	public Typeface getMediumTypeface( AssetManager manager ) {
		return Typeface.createFromAsset( manager, "fonts/XFINITYSans-Med.otf" );
	}

	@Provides
	@Named( "light" )
	@Singleton
	public Typeface getLightTypeface( AssetManager manager ) {
		return Typeface.createFromAsset( manager, "fonts/XFINITYSans-Lgt.otf" );
	}

	@Provides
	@Named( "bold" )
	@Singleton
	public Typeface getBoldTypeface( AssetManager manager ) {
		return Typeface.createFromAsset( manager, "fonts/XFINITYSans-Bold.otf" );
	}

	@Provides
	@Named( "bold condensed" )
	@Singleton
	public Typeface getBoldCondensedTypeface( AssetManager manager ) {
		return Typeface.createFromAsset( manager, "fonts/XFINITYSans-BoldCond.otf" );
	}

	@Provides
	@UiThread
	public Executor createGuiThreadExecutor( final Handler handler ) {
		return new Executor() {
			@Override
			public void execute( Runnable r ) {
				handler.post( r );
			}
		};
	}

	@Provides
	@BackgroundThread
	public Executor createExecutor() {
		return Executors.newScheduledThreadPool( 5 );
	}

	@Provides
	public CurrencyFormat getCurrencyFormat( Locale locale ) {
		return new CurrencyFormat( "\u00A40.00", Currency.getInstance( locale ) );
	}

	@Provides
	public DateFormat getDateFormat( Locale locale ) {
		return DateFormat.getDateInstance( DateFormat.SHORT, locale );
	}

	@Provides
	Ln.Print createLnPrint() {
		return new Ln.Print() {
			SimpleDateFormat format = new SimpleDateFormat( "HH:mm:ss.SSS" );


			@Override
			protected String processMessage( String msg ) {
				return String.format( "%s %s", new Object[] { Thread.currentThread().getName(), msg } );
			}

			private Date getTime() {
				return new Date();
			}

			@Override
			public int println( int priority, String msg ) {
				String temp = String.format( "%s/%s", format.format( getTime() ), getScope( 5 ) );
				return Log.println( priority, temp, processMessage( msg ) );
			}
		};
	}
}
