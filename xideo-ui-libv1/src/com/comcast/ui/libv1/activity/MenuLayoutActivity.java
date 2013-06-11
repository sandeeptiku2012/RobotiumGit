package com.comcast.ui.libv1.activity;

import java.util.ArrayList;
import java.util.List;

import com.comcast.ui.libv1.R;
import com.comcast.ui.libv1.widget.MenuLayout;
import com.comcast.ui.libv1.widget.MenuLayout.MenuItem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MenuLayoutActivity extends Activity {

	String[] menuLabels = {"SEARCH","HOME","SUBSCRIPTIONS","SETTINGS", "FAVORITES", "POPULAR", "SPOTLIGHT", "OTHERS"};
	MenuLayout menu1, menu2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_layout);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		List<MenuItem> items = new ArrayList<MenuItem>();
		for(int i=0;i<menuLabels.length;i++){
			MenuItem item = new MenuItem();
			item.itemLabel = menuLabels[i];
			item.iconDrawable = getResources().getDrawable(R.drawable.ic_launcher);
			items.add(item);
		}
		
		menu1 = (MenuLayout) findViewById(R.id.menu1);
		menu1.addMenuItems(items);
		menu2 = (MenuLayout) findViewById(R.id.menu2);
		menu2.addMenuItems(items);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		menu1.requestFocus();
	}
}
