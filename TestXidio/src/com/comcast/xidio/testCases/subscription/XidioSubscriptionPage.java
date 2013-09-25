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
import com.comcast.xidio.model.GetShowContent;
import com.comcast.xidio.model.GetSubscriptionList;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XidioSubscriptionPage extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;
	
	
	public XidioSubscriptionPage() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testXidioSubscriptionPage() 
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_5000);
		JSONObject loginResponse=GetLoginResponse.getInstance().getLoginResponse(TestConstants.USERNAME,TestConstants.PASSWORD);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		JSONObject response;
		JSONArray channels = null; 
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
				Log.e("Exception:", "Exception occured in XidioSubscriptionPage test case.", e);
			}			
		
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(TestConstants.SLEEP_TIME_1000);
		
		for(int i=0;i<channels.length();i++)
		{
			solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
			solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
			JSONObject currChannel;
			try {
					currChannel = channels.getJSONObject(i);
					solo.sleep(TestConstants.SLEEP_TIME_500);
					String currChannelId;
					assertTrue(solo.searchText(currChannel.getString("title")));
					solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
					currChannelId=currChannel.getString("@id");
					JSONArray showContent=GetShowContent.getInstance().getShowContent(currChannelId);
					if(showContent!=null)
					for(int j=0;j<showContent.length();j++)
					{
						solo.sleep(TestConstants.SLEEP_TIME_500);
						try {
								JSONObject show=showContent.getJSONObject(j);
								assertTrue(solo.searchText(show.getString("title")));
								solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
							}
							catch (JSONException e)
							{
								Log.e("Exception in XidioSubscriptionPage", e.getLocalizedMessage());
							
							}
					}
				}
				catch (JSONException exe)
				{
					Log.e("Exception:", "Exception occured in XidioSubscriptionPage test case.", exe);
				}
			
		}
		
	}
	@Override
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}
	
}


