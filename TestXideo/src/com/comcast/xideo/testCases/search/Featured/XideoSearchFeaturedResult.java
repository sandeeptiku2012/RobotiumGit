package com.comcast.xideo.testCases.search.Featured;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.FilterObject;
import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoSearchFeaturedResult extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private ArrayList<JSONObject> tempItems;


	public XideoSearchFeaturedResult() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(), getActivity().getSessionId());
		super.setUp();
	}

	public void testSearchFeatured() {
		
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_M);
		solo.sendKey(KeyEvent.KEYCODE_A);

		solo.sendKey(KeyEvent.KEYCODE_D);
		solo.sendKey(KeyEvent.KEYCODE_O);
		solo.sendKey(KeyEvent.KEYCODE_N);

		tempItems = FilterObject.getInstance().getFilteredObjectList(GetCatagoryLists.getInstance().getFeaturedList(), "madon");

		solo.sleep(4000);

		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		for (JSONObject currentObject : tempItems) {
			try {
				assertTrue(solo.searchText(currentObject.getString(TestConstants.TITLE)	.toString()));
				solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);

			} catch (JSONException e) {
				Log.e(this.getClass().getCanonicalName(), "Failed to complete the tset XideoSearchFeaturedResult " , e);
				}
		}

	}

}