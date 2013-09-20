package com.comcast.xidio.testCases.home.upNext;

import org.json.JSONArray;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.comcast.xidio.core.common.GetCatagoryLists;
import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.core.XidioApplication;

public class XidioHomeActivityUpNextListTitle extends ActivityInstrumentationTestCase2<FirstRun> 
{
	private Solo solo;

	public XidioHomeActivityUpNextListTitle() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {
		
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(XidioApplication.getLastLoggedInUser(), XidioApplication.getLastSessionId());
		super.setUp();
	}

	public void testXideoHomeActivityUpNextListTitle()	{
		
	 	solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_1000);
		String videoTitle = null;
		solo.sleep(TestConstants.SLEEP_TIME_500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);

		JSONArray upNextArray = GetCatagoryLists.getInstance().getUpNextList();
		if(upNextArray !=null && upNextArray.length()>0)
		{
			for (int i = 0; i < upNextArray.length(); i++)
			{
				JSONObject currentChannel = null;
				try {
					currentChannel = upNextArray.getJSONObject(i);
					videoTitle = currentChannel.getString(TestConstants.TITLE);
	
				} catch (Exception e)
				{
					Log.e(this.getClass().getCanonicalName(), "Failed to get title from XideoHomeActivityUpNextListTitle.java " , e);
				}
				assertTrue(solo.waitForText(videoTitle));
				solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				solo.sleep(TestConstants.SLEEP_TIME_500);
	
			}
		}
	}
 protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}
}
