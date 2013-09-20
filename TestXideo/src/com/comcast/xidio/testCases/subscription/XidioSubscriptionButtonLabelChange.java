package com.comcast.xidio.testCases.subscription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.comcast.xidio.model.GetFeaturedList;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XidioSubscriptionButtonLabelChange extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;	
	
	public XidioSubscriptionButtonLabelChange() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testXideoSubscriptionButtonLabelChange() 
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_5000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		try
		{
			JSONArray featuredList=GetFeaturedList.getInstance().getFeaturedList();
			
			for(int i=0;i<featuredList.length();i++)
			{
				
					JSONObject currChoice=featuredList.getJSONObject(i);
					if(currChoice.has("category"))
						if(currChoice.getJSONObject("category").has("level"))
							if(currChoice.getJSONObject("category").getString("level").trim().contentEquals("SHOW"))
							{
								solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
								solo.waitForActivity(TestConstants.DETAILS_ACTIVITY);
								solo.sleep(TestConstants.SLEEP_TIME_1000);
								solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
								solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
								if(solo.searchText(TestConstants.SUBSCRIBE)){
								solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
								solo.sleep(TestConstants.SLEEP_TIME_2000);
								assertTrue(solo.searchText(TestConstants.UNSUBSCRIBE));
								}
								else
									if(solo.searchText(TestConstants.UNSUBSCRIBE)){
										solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
										solo.sleep(TestConstants.SLEEP_TIME_2000);
										assertTrue(solo.searchText(TestConstants.SUBSCRIBE));
										
									}
								solo.sleep(TestConstants.SLEEP_TIME_2000);
								break;								
							}
					if(!currChoice.has("productGroup"))
							solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				}	
		
		}
		catch (JSONException e) 
		{
			Log.e("Exception:", "Exception occured in testSubscrptionTitle test cases.", e);
		}
		
				
	}
	
	@Override
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
