package com.comcast.xideo.authentication;

import android.os.Bundle;
import android.view.KeyEvent;

import com.comcast.xideo.activity.BaseActivity;
import com.comcast.xideo.R;
import com.comcast.xideo.authentication.ActivationCodeViewModel.ActivationCodeListener;
import com.google.inject.Inject;

public class ActivationCodeDialog extends BaseActivity implements ActivationCodeListener {
	@Inject
	public ActivationCodeViewModel viewModel;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setAndBindRootView( R.layout.activationcode, viewModel );
		viewModel.setListener( this );
	}

	@Override
	public void onLoggedIn() {
		finish();
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		switch( keyCode ) {
			case KeyEvent.KEYCODE_BACK:
				return true;
			default:
				return super.onKeyDown( keyCode, event );
		}
	}
}
