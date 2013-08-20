package com.comcast.xideo.testCases.search.Episodes;

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
import com.comcast.xideo.model.GetEpisodeSearchList;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoSearchEpisodeResultTitle extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;


	public XideoSearchEpisodeResultTitle() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(), getActivity());
		solo = GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(),getActivity().getSessionId());
		super.setUp();
	}

	public void test_Search_Channel() {
		
		solo.sleep(5000);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_A);
		solo.sendKey(KeyEvent.KEYCODE_B);
		solo.sendKey(KeyEvent.KEYCODE_R);

		ArrayList<JSONObject> tempItems = FilterObject.getInstance().getFilteredObjectList(GetEpisodeSearchList.getInstance().getEpisodeSearchList("abr"), "abr");

		solo.sleep(4000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);

		for (JSONObject currentObject : tempItems) {
			try {
				String channelTitle = currentObject.getString(TestConstants.TITLE);
				solo.sleep(200);
				solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
				assertTrue(solo.waitForActivity(TestConstants.VIDEOPLAYER_ACTIVITY));
				solo.sleep(500);
				
				assertTrue(solo.searchText(channelTitle.toString()));
				
				solo.sleep(200);
				solo.sendKey(KeyEvent.KEYCODE_BACK);
				solo.sleep(500);
				solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				solo.sleep(200);
			} catch (JSONException e) {
				Log.e(this.getClass().getCanonicalName(), "Failed to complete the tset XideoSearchEpisodeResultTitle " , e);			}
		}

	}
}
