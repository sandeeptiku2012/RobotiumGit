package com.comcast.xideo.activity;

import com.comcast.xideo.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;

public class SettingsActivity extends BaseActivity {
	//@InjectView( R.id.versionTextView )
	//	private TextView versionTextView;
String VersionName;
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.settings_layout );
		updateVersionString();
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		switch( keyCode ) {
			case KeyEvent.KEYCODE_MOVE_HOME:
				startActivity( createIntent( getApplicationContext() ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
				return true;
		}
		return super.onKeyDown( keyCode, event );
	}

	private void updateVersionString() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo( getPackageName(), 0 );
		 VersionName = packageInfo.versionName;
			 
		} catch( NameNotFoundException e ) {
			e.printStackTrace();
		}
	}

	public void onOpenNetworkSettingsClick( View view ) {
		startActivity( new Intent( Settings.ACTION_WIFI_SETTINGS ) );
	}

	public void onOpenDisplaySettings( View view ) {
		startActivity( new Intent( Settings.ACTION_DISPLAY_SETTINGS ) );
	}
	
	public void onOpenAboutSettings(View view)
	{
	 Intent i=new Intent();
		 i.putExtra("VER", VersionName);
		 i.setClass(getApplicationContext(), AboutList.class);
		 startActivity(i);
		
	}

	public static Intent createIntent( Context context ) {
		return new Intent( context, SettingsActivity.class );
	}
}
