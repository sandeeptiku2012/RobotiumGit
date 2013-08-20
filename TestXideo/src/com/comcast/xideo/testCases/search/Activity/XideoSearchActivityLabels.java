package com.comcast.xideo.testCases.search.Activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.FilterObject;
import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.comcast.xideo.model.GetEpisodeSearchList;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoSearchActivityLabels extends ActivityInstrumentationTestCase2<MainActivity> 
{
	private Solo solo;

	public XideoSearchActivityLabels() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception
	{
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(), getActivity().getSessionId());
		super.setUp();
	}

	public void testSearchLabels() {

		ArrayList<JSONObject> episodeSearchItems, featuredSearchItems, upnextSearchItems, popularSearchItems;
		String filterText = "abr";
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_A);
		solo.sendKey(KeyEvent.KEYCODE_B);
		solo.sendKey(KeyEvent.KEYCODE_R);
		
		episodeSearchItems = FilterObject.getInstance().getFilteredObjectList(GetEpisodeSearchList.getInstance().getEpisodeSearchList(filterText), filterText);
		featuredSearchItems = FilterObject.getInstance().getFilteredObjectList(GetCatagoryLists.getInstance().getFeaturedList(), filterText);
		popularSearchItems = FilterObject.getInstance().getFilteredObjectList(GetCatagoryLists.getInstance().getPopularList(), filterText);
		upnextSearchItems = FilterObject.getInstance().getFilteredObjectList(GetCatagoryLists.getInstance().getUpNextList(), filterText);
		
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(500);
		
		if (upnextSearchItems.size() != 0)
		{
			assertTrue(solo.waitForText(TestConstants.UP_NEXT));
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		solo.sleep(500);
		if (featuredSearchItems.size() != 0) {
			assertTrue(solo.waitForText(TestConstants.FEATURE));
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);

		}
		solo.sleep(500);
		if (popularSearchItems.size() != 0) {
			assertTrue(solo.waitForText(TestConstants.POPULAR));
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		if (episodeSearchItems.size() != 0) {
			assertTrue(solo.waitForText(TestConstants.MATCHING_EPISODES));
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		solo.sleep(2000);

	}

}
