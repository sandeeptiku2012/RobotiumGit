package com.comcast.xideo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comcast.xideo.R;
import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.events.VideoItemEvent;
import com.comcast.xideo.viewmodel.SubscriptionViewModel;
import com.comcast.xideo.viewmodel.VideoInfoViewModel;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;

@SuppressLint("NewApi")
public class SubscriptionFragment extends BaseFragment {
	@Inject
	private SubscriptionViewModel model;

	public SubscriptionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return setAndBindRootView(R.layout.subs_frag, container, model);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		model.start();
	}

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

	@Subscribe
	public void onSubscriptionItemSelectedEvent(VideoItemEvent e) {
		Log.e("", e.toString());
		Object obj = e.getValue();
		if (obj instanceof VideoInfoViewModel) {
			VideoInfoViewModel vivm = (VideoInfoViewModel) obj;
			long aid = vivm.assetId.get();

			// Intent intent = new Intent(this.getActivity(),
			// XidioShowActivity.class);
			// intent.putExtra("assetId", aid);
			// startActivity(intent);
		}
	}
}
