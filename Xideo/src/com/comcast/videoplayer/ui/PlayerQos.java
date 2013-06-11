/*************************************************************************
*
* ADOBE CONFIDENTIAL
* ___________________
*
*  Copyright 2012 Adobe Systems Incorporated
*  All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains
* the property of Adobe Systems Incorporated and its suppliers,
* if any.  The intellectual and technical concepts contained
* herein are proprietary to Adobe Systems Incorporated and its
* suppliers and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from Adobe Systems Incorporated.
**************************************************************************/

package com.comcast.videoplayer.ui;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import roboguice.fragment.RoboFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.comcast.xideo.R;
import com.comcast.xideo.XideoApplication;

public class PlayerQos extends RoboFragment {
	private final String LOG_TAG = "[XideoApplication]::PlayerQos";
	private ArrayList<QosItem> qosItems = new ArrayList<QosItem>();
	private ViewGroup viewGroup;

	static class QosItem {
		private String name;
		private Object value;
		private int id;
		private static final AtomicInteger uniqueId = new AtomicInteger();

		public QosItem(String name, Object value) {
			this.name = name;
			this.value = value;

			// This is thread safe.
			this.id = uniqueId.getAndIncrement();
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value.toString();
		}

		public int getId() {
			return id;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}

	private boolean isQosEnabled() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		return sharedPreferences.getBoolean( XideoApplication.SETTINGS_QOS_VISIBILITY, XideoApplication.DEFAULT_QOS_VISIBILITY );
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// XideoApplication.logger.i(LOG_TAG, "Creating QoS view.");
		viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_player_qos, container, false);
		updateQosView();
		return viewGroup;
	}

	public void updateQosView() {
		if (!isQosEnabled() || viewGroup == null) {
			// QoS is not activated (anymore).
			if (viewGroup != null)
				viewGroup.setVisibility(View.GONE);
			return;
		}

		LayoutInflater inflater = getActivity().getLayoutInflater();
		for (QosItem item : qosItems) {
			// Check if item view is already inflated.
			View tv = viewGroup.findViewById(item.getId());
			if (tv == null) {
				// Inflate the text view.
				tv = inflater.inflate(R.layout.fragment_player_qos, null).findViewById(R.id.qosTextView);
				tv.setId(item.getId());
			}
			// Set the new text value.
			((TextView) tv).setText(item.getName() + ": " + item.getValue());
			viewGroup.addView(removeFromParent(tv), new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		viewGroup.setVisibility(View.VISIBLE);
	}

	public void setQosItem(String name, Object value, String metric) {
		setQosItem(name, value + " " + metric);
	}

	public void setQosItem(String name, Object value) {
		boolean found = false;
		for (QosItem item : qosItems) {
			if (item.getName().equalsIgnoreCase(name)) {
				item.setValue(value);
				found = true;
			}
		}
		if (!found)
			qosItems.add(new QosItem(name, value));
		updateQosView();
	}

	public void setQosItem(int nameResourceId, Object value) {
		setQosItem(getString(nameResourceId), value);
	}

	private static View removeFromParent(View view) {
		if (view == null || view.getParent() == null)
			return view;
		((ViewGroup) view.getParent()).removeView(view);
		return view;
	}

	public void updateQosInformation(int bitRate) {
		setQosItem("Bitrate", bitRate);
	}
}
