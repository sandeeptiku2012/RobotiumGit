package com.comcast.xideo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comcast.xideo.R;
import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.events.ChannelItemEvent;
import com.comcast.xideo.viewmodel.Category;
import com.comcast.xideo.viewmodel.ContentPanelViewModel;
import com.comcast.xideo.viewmodel.HomeViewModel;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.squareup.otto.Subscribe;
import com.vimond.entity.ContentPanelType;

@SuppressLint("NewApi")
public class HomeFragment extends BaseFragment {
	@Inject
	private HomeViewModel model;

	@Inject
	private Injector injector;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return setAndBindRootView(R.layout.home_frag, container, model);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		downloadContent();
	};

	@Override
	public void onResume() {
		super.onResume();
		XideoApplication.getBus().register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		XideoApplication.getBus().unregister(this);
	}

	void downloadContent() {
		Category category = new Category( 100 );

		model.contentRows.clear();

		ContentPanelViewModel featured = new ContentPanelViewModel( "Featured", ContentPanelType.FEATURED );
		injector.injectMembers( featured );
		model.contentRows.add( featured );
		featured.start( category );

		ContentPanelViewModel popular = new ContentPanelViewModel( "Popular", ContentPanelType.POPULAR );
		injector.injectMembers( popular );
		model.contentRows.add( popular );
		popular.start( category );

		ContentPanelViewModel noteworthy = new ContentPanelViewModel( "Recommended", ContentPanelType.RECOMMENDED );
		injector.injectMembers( noteworthy );
		model.contentRows.add( noteworthy );
		noteworthy.start( category );
		model.isLoading.set( false );
	}

	@Subscribe
	public void onHomeItemSelectedEvent(ChannelItemEvent e) {
	}

	// ./adb shell input keyevent 19 - DPAD up
	// ./adb shell input keyevent 20 - DPAD down
	// ./adb shell input keyevent 21 - DPAD left
	// ./adb shell input keyevent 22 - DPAD right

}
