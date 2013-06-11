package com.comcast.xideo.authentication;

import gueei.binding.Command;
import gueei.binding.observables.StringObservable;
import no.knowit.misc.GoldenAsyncTask;
import android.view.View;

import com.google.inject.Inject;
import com.vimond.service.StbAuthenticationService;

public class ActivationCodeViewModel {
	interface ActivationCodeListener {
		void onLoggedIn();
	}

	public final StringObservable activationCode = new StringObservable();
	public final StringObservable errorMessage = new StringObservable();

	public final Command login = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			errorMessage.set( null );
			new GoldenAsyncTask<Void>() {
				@Override
				public Void call() throws Exception {
					authenticationService.loginSilently();
					return null;
				}

				@Override
				protected void onSuccess( Void result ) throws Exception {
					listener.onLoggedIn();
				};

				@Override
				protected void onException( Exception e ) throws RuntimeException {
					errorMessage.set( "ERROR! " + e );
				};
			}.execute();
		}
	};

	private final StbAuthenticationService authenticationService;
	private ActivationCodeListener listener;

	@Inject
	public ActivationCodeViewModel( StbAuthenticationService service ) {
		this.authenticationService = service;
		activationCode.set( service.getActivationCode() );
	}

	public void setListener( ActivationCodeListener listener ) {
		this.listener = listener;
	}
}
