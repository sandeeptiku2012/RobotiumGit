package com.comcast.xideo.activity;

import gueei.binding.IObservable;
import gueei.binding.Observer;

import java.util.Collection;

import roboguice.event.EventThread;
import roboguice.event.Observes;
import roboguice.util.Ln;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.comcast.xideo.MissingUserError;
import com.comcast.xideo.NetworkError;
import com.comcast.xideo.R;
import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.viewmodel.XideoModel;
import com.google.inject.Inject;
import com.vimond.service.StbAuthenticationService;

public class XideoSplashActivity extends BaseActivity {

	@Inject
	private XideoModel model;

	@Inject
	private StbAuthenticationService authservice;

	@Inject
	FragmentManager fragmentManager;
	
	View loadingImage;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_xideo_splash );
		
		loadingImage = findViewById( R.id.image1 );
		XideoApplication.setModel( model );
		model.isLoading.subscribe( loadingObserver );
	}

	Observer loadingObserver = new Observer() {
		@Override
		public void onPropertyChanged( IObservable<?> arg0, Collection<Object> arg1 ) {
			if (model.isLoading.get() == false) {
				model.isLoading.unsubscribe( this );
				showHomeActivity();
			}
		}
	};

	RotateAnimation rotate;
	void startAnimation() {
		rotate = new RotateAnimation( 0, 360, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
		rotate.setDuration( 2000 );
		rotate.setInterpolator( new LinearInterpolator() );
		rotate.setRepeatCount( Animation.INFINITE );
		loadingImage.startAnimation( rotate );
	}

	public void onNetworkError( @Observes NetworkError error ) {
		Ln.i( "onNetworkError" );
//		startActivity( new Intent( this, NetworkErrorActivity.class ) );
	}

	public void onMissingUserError( @Observes( EventThread.UI ) MissingUserError error ) {
		Ln.i( "onMissingUserError" );
		CommonActions.checkUser( this, authservice );
	}

	@Override
	protected void onResume() {
		super.onResume();
		// CommonActions.checkUser(this, authservice);
		startAnimation();
		model.sync();
	}

	private void showHomeActivity() {
		rotate.cancel();
		finish();
		Intent intent = new Intent( XideoSplashActivity.this.getApplicationContext(), XideoHomeActivity.class );
		startActivity( intent );
	}
}
