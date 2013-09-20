package com.comcast.xidio.testCases.home.featured;

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
import com.xfinity.xidio.MainActivity;
import com.xfinity.xidio.core.XidioApplication;

public class XideoDetailsActivityChange extends ActivityInstrumentationTestCase2<FirstRun> {
	private Solo solo;

	public XideoDetailsActivityChange() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(XidioApplication.getLastLoggedInUser(), XidioApplication.getLastSessionId());
		super.setUp();
	}

	public void testXideoDetailsActivityChange() 
	{
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_5000);
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		try{
		JSONArray featuredJsonArray = GetCatagoryLists.getInstance().getFeaturedList();
		if(featuredJsonArray!=null && featuredJsonArray.length()>0)
		{
			
			
			for(int j=0;j<featuredJsonArray.length();j++)
			{
				JSONObject currElement=featuredJsonArray.getJSONObject(j);
				if(currElement.has("category"))
				{	if(currElement.getJSONObject("category").has("level"))
						{
							if(currElement.getJSONObject("category").getString("level").trim().equalsIgnoreCase("SUB_SHOW"))
								{   solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
									solo.sleep(TestConstants.SLEEP_TIME_500);
									assertTrue(solo.waitForActivity(TestConstants.DETAILS_ACTIVITY));
									solo.sleep(TestConstants.SLEEP_TIME_2000);
									solo.sendKey(KeyEvent.KEYCODE_BACK);
									solo.sleep(TestConstants.SLEEP_TIME_500);
									assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
									continue;
								}
							else if(currElement.getJSONObject("category").getString("level").trim().equalsIgnoreCase("SHOW")){
								solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
								solo.sleep(TestConstants.SLEEP_TIME_500);
								assertTrue(solo.waitForActivity(TestConstants.DETAILS_ACTIVITY));
								solo.sleep(TestConstants.SLEEP_TIME_2000);
								solo.sendKey(KeyEvent.KEYCODE_BACK);
								assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
								solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
								continue;
								
							}
						}
				
				}			
				else if(currElement.has("asset"))
				{	
					solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
					solo.sleep(TestConstants.SLEEP_TIME_500);
					assertTrue(solo.waitForActivity(TestConstants.VIDEOPLAYER_ACTIVITY));
					solo.sleep(TestConstants.SLEEP_TIME_500);
					solo.sendKey(KeyEvent.KEYCODE_BACK);
					solo.sleep(TestConstants.SLEEP_TIME_2000);
					assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
					solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
					continue;
				}
				solo.sleep(TestConstants.SLEEP_TIME_500);
			}
			
		}	

	}
		
	catch(Exception e)
	{
		Log.d("Exception from testDetailsActivityChange = ", e.getLocalizedMessage());
	}
	

	}
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}
}
