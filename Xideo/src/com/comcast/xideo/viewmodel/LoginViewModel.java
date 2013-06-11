package com.comcast.xideo.viewmodel;

import android.view.View;

import gueei.binding.Command;
import gueei.binding.observables.StringObservable;
import no.knowit.misc.GoldenAsyncTask;

import com.google.inject.Inject;
import com.vimond.service.StbAuthenticationService;

public class LoginViewModel {
	public interface LoginListener {
		void onSuccessfulLogin();
	}

	@Inject
	StbAuthenticationService authservice;

	public final StringObservable username = new StringObservable();
	public final StringObservable password = new StringObservable();
	public final StringObservable message = new StringObservable();

	private LoginListener listener;

	public Command login = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			new GoldenAsyncTask<Void>() {
				@Override
				public Void call() throws Exception {
					authservice.login( username.get(), password.get() );
					return null;
				}

				@Override
				protected void onSuccess( Void t ) throws Exception {
					if( listener != null ) {
						listener.onSuccessfulLogin();
					}
				};

				@Override
				protected void onException( Exception e ) throws RuntimeException {
					message.set( e.getLocalizedMessage() );
				};
			}.execute();
		}
	};

	public void setListener( LoginListener listener ) {
		this.listener = listener;
	}
}
