package com.comcast.xideo.activity;

import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
import gueei.binding.menu.OptionsMenuBinder;
import roboguice.activity.RoboActivity;
import roboguice.activity.RoboFragmentActivity;
import roboguice.util.Ln;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.comcast.xideo.Constants;
import com.comcast.xideo.InjectLayout;
import com.goldengate.ui.GoldenGateLayoutInflater;
import com.google.inject.Inject;
import com.vimond.errorreporting.ErrorReportSender;

public class BaseActivity extends RoboFragmentActivity implements Thread.UncaughtExceptionHandler {
	@Inject
	private LayoutInflater inflater;

	@Inject
	private GoldenGateLayoutInflater factory;

	@Inject
	private ErrorReportSender errorReportSender;

	protected static Typeface defaultTypeface;
	protected static Typeface boldTypeface;
	private static boolean startedLogin = false;

	BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive( Context context, Intent intent ) {
			Log.w( "Network Listener", "Network Type Changed" );
			if( intent.getExtras().getBoolean( ConnectivityManager.EXTRA_NO_CONNECTIVITY ) == true ) {
				if( !startedLogin ) {
					if( !isOnline() ) {
						startActivityForResult( new Intent( BaseActivity.this, NetworkErrorActivity.class ), NetworkErrorActivity.RETRY_DOWNLOAD );
					}

				}
				startedLogin = true;
			}
		}
	};

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		// Set up so that we get all exceptions
		Thread.setDefaultUncaughtExceptionHandler( this );

		if( inflater.getFactory() == null ) {
			inflater.setFactory( factory );
		}

		InjectLayout attribute = getClass().getAnnotation( InjectLayout.class );
		if( attribute != null ) {
			setContentView( attribute.value() );
		}

		IntentFilter filter = new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION );
		registerReceiver( networkStateReceiver, filter );
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver( networkStateReceiver );
	}

	public void onCommonTaskCallBack() {

	}

	protected void networkRetry() {

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if( netInfo != null && netInfo.isConnectedOrConnecting() ) {
			return true;
		}
		return false;
	}

	@Override
	public void uncaughtException( Thread thread, Throwable throwable ) {
		try {
			errorReportSender.send( throwable.getMessage(), throwable );
		} catch( Throwable e ) {
			Ln.e( e, "Got exception inside uncaughtException-handler" );
		}
	}

	OptionsMenuBinder menuBinder;
	Object mMenuViewModel;
	private View mRootView;

	protected View setAndBindRootView( int layoutId, Object... contentViewModel ) {
		if( mRootView != null ) {
			throw new IllegalStateException( "Root view is already created" );
		}
		InflateResult result = Binder.inflateView( this, layoutId, null, false );
		mRootView = result.rootView;
		for( Object element : contentViewModel ) {
			Binder.bindView( this, result, element );
		}
		setContentView( mRootView );
		return mRootView;
	}

	protected void setAndBindOptionsMenu( int menuId, Object menuViewModel ) {
		if( menuBinder != null ) {
			throw new IllegalStateException( "Options menu can only set once" );
		}
		menuBinder = new OptionsMenuBinder( menuId );
		mMenuViewModel = menuViewModel;
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// No menu is defined
		if( menuBinder == null ) {
			return false;
		}
		return menuBinder.onCreateOptionsMenu( this, menu, mMenuViewModel );
	}

	@Override
	public boolean onPrepareOptionsMenu( Menu menu ) {
		if( menuBinder == null ) {
			return false;
		}
		return menuBinder.onPrepareOptionsMenu( this, menu );
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		if( menuBinder != null ) {
			return menuBinder.onOptionsItemSelected( this, item );
		}
		return super.onOptionsItemSelected( item );
	}

	public View getRootView() {
		return mRootView;
	}

	public void setRootView( View rootView ) {
		mRootView = rootView;
	}

	public Typeface getDefaultTypeface() {
		if( defaultTypeface == null ) {
			defaultTypeface = Typeface.createFromAsset( getAssets(), "fonts/XFINITYSans-Med.otf" );
		}
		return defaultTypeface;
	}

	public Typeface getBoldTypeface() {
		if( boldTypeface == null ) {
			boldTypeface = Typeface.createFromAsset( getAssets(), "fonts/XFINITYSans-Bold.otf" );
		}
		return boldTypeface;
	}

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
		super.onActivityResult( requestCode, resultCode, data );
		if( data != null && data.getBooleanExtra( Constants.IntentIdentifiers.SHOW_MY_CHANNELS, false ) ) {
			setResult( Activity.RESULT_OK, data );
			finish();
		}
	}
}