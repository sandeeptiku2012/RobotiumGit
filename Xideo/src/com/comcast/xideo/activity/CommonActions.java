package com.comcast.xideo.activity;

import no.knowit.misc.GoldenAsyncTask;
import roboguice.util.Ln;
import android.app.Activity;
import android.content.Intent;

import com.comcast.xideo.activity.BaseActivity;
import com.comcast.xideo.authentication.ActivationCodeDialog;
import com.vimond.service.StbAuthenticationService;

public class CommonActions {
	public static void checkUser( final BaseActivity activity, final StbAuthenticationService authserv ) {
		new GoldenAsyncTask<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				Ln.i( "checkUser - call" );
				return authserv.getUser() != null;
			}

			@Override
			protected void onSuccess( Boolean isLoggedIn ) throws Exception {
				Ln.i( "checkUser - onSuccess - isLoggedIn = %s", isLoggedIn );
				super.onSuccess( isLoggedIn );
				if( !isLoggedIn ) {
					showLoginDialog( activity );
				} else {
					activity.onCommonTaskCallBack();
				}
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				Ln.i( "checkUser - onException" );
				showLoginDialog( activity );
			}
		}.execute();
	}

	private static void showLoginDialog( final Activity activity ) {
		activity.startActivity( new Intent( activity, ActivationCodeDialog.class ) );
	}
}
