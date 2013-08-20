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
import com.comcast.xideo.model.GetLoginResponse;
import com.comcast.xideo.model.GetShowContent;
import com.comcast.xideo.model.GetSubscriptionList;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoSubscriptionFeaturedApiTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	boolean loggedIn=false;
	private	String userId=null;
	
	
	public XideoSubscriptionFeaturedApiTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		//GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(),getActivity().getSessionId());
		super.setUp();
	}

	public void test_Subscrption_Title() 
	{
		solo.sleep(2000);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		JSONObject loginResponse=GetLoginResponse.getInstance().getLoginResponse("test_151","Demo1111");
		JSONArray channels = null; 
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(2000);
		JSONObject channelToCheck=null;
		try {
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
							solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
							solo.sleep(1000);
							solo.sendKey(KeyEvent.KEYCODE_BACK);
							solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
							solo.sleep(1000);
							solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
							solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
							solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
							
							channelToCheck=currChoice;
							break;
							
						}
				if(!currChoice.has("productGroup"))
							solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		}

		
		
		
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	//	solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(2000);
		JSONObject response;
		
		if(loginResponse.has("response"))
		try {
				response=loginResponse.getJSONObject("response");
			
			if(response.has("code"))
			if(response.getString("code").equalsIgnoreCase("AUTHENTICATION_OK"))
				{
					userId=response.getString("userId");
					channels=GetSubscriptionList.getInstance().getSubscriptionList(userId,getActivity().getApplicationContext());
				}
			} catch (JSONException e) {
				Log.e("test_Subscrption_Title", e.getLocalizedMessage());
			}
			
		JSONObject currChannel;
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(1000);
		boolean foundChannel = false;
		try {
		for(int i=0;i<channels.length();i++)
		{
			
				currChannel = channels.getJSONObject(i);
			
			if(currChannel.getString("title").trim().contentEquals(channelToCheck.getString("title").trim()))
			{
				foundChannel=true;
				break;
			}
			
		}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		assertTrue(foundChannel);
				
	}
}
