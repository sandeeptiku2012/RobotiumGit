package com.comcast.xidio.testCases.subscription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.comcast.xidio.model.GetLoginResponse;
import com.comcast.xidio.model.GetPopularList;
import com.comcast.xidio.model.GetSubscriptionList;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XideoSubscriptionPopularUITest  extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;
	
	public XideoSubscriptionPopularUITest() 
	{
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testXideoSubscriptionPopularUITest() 
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
		JSONObject loginResponse=GetLoginResponse.getInstance().getLoginResponse(TestConstants.USERNAME,TestConstants.PASSWORD);
		JSONArray channels = null; 
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(2000);
		
		JSONObject channelToCheck=null;
		try 
		{
		
			JSONArray popularList=GetPopularList.getInstance().getPopularList();
		
				for(int i=0;i<popularList.length();i++)
				{
					
						JSONObject currChoice=popularList.getJSONObject(i);
						if(currChoice.has("category"))
							if(currChoice.getJSONObject("category").has("level"))
								if(currChoice.getJSONObject("category").getString("level").trim().contentEquals("SHOW"))
								{
									solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
									solo.waitForActivity(TestConstants.DETAILS_ACTIVITY);
									solo.sleep(1000);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
									solo.sleep(1000);
									solo.sendKey(KeyEvent.KEYCODE_BACK);
									solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
									solo.sleep(1000);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);									
									channelToCheck=currChoice;
									break;									
								}
						if(!currChoice.has("productGroup"))
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				}	
		
		} 
		catch (JSONException e) {
			Log.e("Exception:", "Exception occured in testSubscrptionPopularApi test case.", e);
		}
		
		solo.sleep(2000);
		JSONObject response;
		
		if(loginResponse.has("response"))
		try {
				response=loginResponse.getJSONObject("response");
			
			if(response.has("code"))
			if(response.getString("code").equalsIgnoreCase("AUTHENTICATION_OK"))
				{
					String userId=response.getString("userId");
					channels=GetSubscriptionList.getInstance().getSubscriptionList(userId);
				}
			} 
			catch (JSONException e)
			{
				Log.e("Exception occured in testSubscrptionPopularUI test case", e.getLocalizedMessage());
			}
			
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(1000);
		boolean foundChannel = false;
		try {
			for(int i=0;i<channels.length();i++)
			{
				foundChannel=solo.searchText(channelToCheck.getString("title"));
				if(foundChannel)
					break;
				else
				{	solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
					solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
				}
				
			}
		} catch (JSONException e) {
			
			Log.e("Exception occured in testSubscrptionPopularUI test case get channel title ", e.getLocalizedMessage());
		}
		
		assertTrue(foundChannel);
				
	}
	
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}
}
