package com.comcast.xidio.testCases.search.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xidio.core.common.FilterObject;
import com.comcast.xidio.core.common.GetCatagoryLists;
import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.comcast.xidio.model.GetEpisodeSearchList;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.core.XidioApplication;

public class XidioSearchActivityNegetiveSearch extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;

	public XidioSearchActivityNegetiveSearch() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception 
	{
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(XidioApplication.getLastLoggedInUser(), XidioApplication.getLastSessionId());
		super.setUp();
	}

	public void testXidioSearchActivityNegativeSearch() 
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_1000);
		ArrayList<JSONObject> episodeSearchItems, featuredSearchItems, upnextSearchItems, popularSearchItems;
		String filterText = "abbr";
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		solo.sendKey(KeyEvent.KEYCODE_A);
		solo.sendKey(KeyEvent.KEYCODE_B);
		solo.sendKey(KeyEvent.KEYCODE_B);
		solo.sendKey(KeyEvent.KEYCODE_R);
		JSONArray episodeSearchArray = GetEpisodeSearchList.getInstance().getEpisodeSearchList(filterText);
		episodeSearchItems = FilterObject.getInstance().getFilteredObjectList(episodeSearchArray, filterText);
		featuredSearchItems = FilterObject.getInstance().getFilteredObjectList(GetCatagoryLists.getInstance().getFeaturedList(), filterText);
		popularSearchItems = FilterObject.getInstance().getFilteredObjectList(GetCatagoryLists.getInstance().getPopularList(), filterText);
		upnextSearchItems = FilterObject.getInstance().getFilteredObjectList(GetCatagoryLists.getInstance().getUpNextList(), filterText);
		
		solo.sleep(TestConstants.SLEEP_TIME_500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		if (upnextSearchItems!=null && upnextSearchItems.size() != 0)
		{
			assertFalse(solo.waitForText(TestConstants.UP_NEXT));
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		solo.sleep(TestConstants.SLEEP_TIME_500);
		if (featuredSearchItems.size() != 0)
		{
			assertFalse(solo.waitForText(TestConstants.FEATURE));

		}
		solo.sleep(TestConstants.SLEEP_TIME_500);
		if (popularSearchItems.size() != 0) {
			assertFalse(solo.waitForText(TestConstants.POPULAR));
		}

	}
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
