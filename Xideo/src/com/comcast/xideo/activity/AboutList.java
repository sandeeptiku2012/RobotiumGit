package com.comcast.xideo.activity;

import java.util.Arrays;

import com.comcast.xideo.R;
import com.comcast.xideo.utils.CAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AboutList extends ListActivity {
  LinearLayout dummy;
	String version_number;
	 //list to display on the screen
	
	String[] settings={"Xidio App Version", "Wifi IP Address", "Ethernet IP" ,"Build Number", "Kernel Version" , "Bluetooth MAC Address", "Android Version"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	 Arrays.sort(settings);
		
		Intent i=getIntent();
		version_number=i.getStringExtra("VER");
		ListView lv=getListView();
		lv.setFocusableInTouchMode(false);
		lv.setFocusable(false);
		lv.clearFocus();
		lv.setBackgroundResource(R.drawable.rounded_rectangle);
		
		setListAdapter(new CAdapter(this, settings,version_number));
		
	}

	

}
