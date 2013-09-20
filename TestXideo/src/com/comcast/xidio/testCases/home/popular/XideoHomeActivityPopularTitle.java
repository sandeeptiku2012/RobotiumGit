package com.comcast.xidio.testCases.home.popular;

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

public class XideoHomeActivityPopularTitle extends ActivityInstrumentationTestCase2<FirstRun> 
{
	private Solo solo;
	
	public XideoHomeActivityPopularTitle() {
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
	
	public void testXideoHomeActivityPopularTitle() 
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_5000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		try {
			JSONArray popularJsonArray = GetCatagoryLists.getInstance().getPopularList();
			if(popularJsonArray!=null && popularJsonArray.length()>0)
			{
			
				for (int count = 0; count < popularJsonArray.length(); count++) 
				{
					JSONObject currentChannel = popularJsonArray.getJSONObject(count);
	
					if ((currentChannel.has("productGroup") || !currentChannel.has("category")) && !currentChannel.has("asset"))
								continue;
					String channelTitle =null;
					if(currentChannel.has("category"))
					{	if(currentChannel.getJSONObject("category").has("title"))
						
						channelTitle = currentChannel.getJSONObject("category").getString("title");
					}
					else if(currentChannel.has("asset"))
					{
						if(currentChannel.getJSONObject("asset").has("title"))
							channelTitle = currentChannel.getJSONObject("asset").getString("title");
					}
					if(channelTitle!=null && channelTitle.length()>0)
					{
						solo.sleep(TestConstants.SLEEP_TIME_500);
						assertTrue(solo.waitForText(channelTitle));
					}
					
					solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				}
			
			}
		} catch (Exception e) 
		{
			Log.e(this.getClass().getCanonicalName(), "Failed to complete the tset XideoHomeActivityFeaturedListTitle " , e);
			assertTrue(false);
		}
		solo.sleep(TestConstants.SLEEP_TIME_2000);
	}
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
