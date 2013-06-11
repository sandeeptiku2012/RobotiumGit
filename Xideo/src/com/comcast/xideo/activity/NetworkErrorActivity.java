package com.comcast.xideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.comcast.xideo.R;
import com.goldengate.ui.GoldenGateLayoutInflater;
import com.google.inject.Inject;
import com.vimond.service.StbAuthenticationService;

public class NetworkErrorActivity extends BaseActivity {
	
	public static final int RETRY_DOWNLOAD = 3;

	@Inject
	private LayoutInflater inflater;

	@Inject
	private StbAuthenticationService authservice;

	@Inject
	private GoldenGateLayoutInflater factory;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_error);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_network_error, menu);
        return true;
    }
    public void tryAgainClick(View v) {
    	setResult( RETRY_DOWNLOAD );
    	finish();
    }
    public void settingsClick(View v) {
		startActivity( new Intent( Settings.ACTION_WIFI_SETTINGS ) );
    }
    
}
