package com.comcast.xideo.activity;

import com.comcast.xideo.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class SettingsFragment extends BaseFragment {

	public SettingsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return setAndBindRootView(R.layout.settings_frag, container, new Object());
		
	}
}
