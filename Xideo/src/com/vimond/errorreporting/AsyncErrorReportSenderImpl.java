package com.vimond.errorreporting;

import android.os.AsyncTask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import roboguice.util.Ln;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;

public class AsyncErrorReportSenderImpl implements ErrorReportSender {

	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD } )
	@BindingAnnotation
	public @interface RealErrorReportSender {
	}

	@Inject
	@RealErrorReportSender
	private ErrorReportSender realErrorReportSender;

	public static class AsyncSender extends AsyncTask<Object, Void, Object> {

		private final String errorMsg;
		private final Throwable e;
		private final ErrorReportSender realReportSender;

		public AsyncSender( String errorMsg, Throwable e, ErrorReportSender realReportSender ) {
			this.errorMsg = errorMsg;
			this.e = e;
			this.realReportSender = realReportSender;
		}

		@Override
		protected Void doInBackground( Object... objects ) {
			try {
				realReportSender.send( errorMsg, e );
			} catch( Throwable ex ) {
				Ln.e( ex, "Got error while sending error report" );
			}
			return null;
		}
	}

	@Override
	public void send( String errorMsg, Throwable e ) {
		try {
			new AsyncSender( errorMsg, e, realErrorReportSender ).execute();
		} catch( Throwable e1 ) {
			Ln.e( e1, "Error starting async sending" );
		}
	}
}
