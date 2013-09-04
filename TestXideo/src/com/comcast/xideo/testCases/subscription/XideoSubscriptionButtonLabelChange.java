package com.comcast.xideo.testCases.subscription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.comcast.xideo.model.GetFeaturedList;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XideoSubscriptionButtonLabelChange extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;	
	
	public XideoSubscriptionButtonLabelChange() {
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
		solo.sleep(1000);
		solo.sleep(2000);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(2000);
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
								solo.sleep(1000);
								solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
								solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
								if(solo.searchText(TestConstants.SUBSCRIBE)){
								solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
								solo.sleep(2000);
								assertTrue(solo.searchText(TestConstants.UNSUBSCRIBE));
								}
								else
									if(solo.searchText(TestConstants.UNSUBSCRIBE)){
										solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
										solo.sleep(2000);
										assertTrue(solo.searchText(TestConstants.SUBSCRIBE));
										
									}
								solo.sleep(2000);
								//assertTrue(solo.searchText(TestConstants.SUBSCRIBE));
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
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
