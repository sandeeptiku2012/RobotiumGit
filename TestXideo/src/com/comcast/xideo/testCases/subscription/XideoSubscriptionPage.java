package com.comcast.xideo.testCases.subscription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.comcast.xideo.model.GetLoginResponse;
import com.comcast.xideo.model.GetShowContent;
import com.comcast.xideo.model.GetSubscriptionList;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoSubscriptionPage extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	boolean loggedIn=false;
	private	String userId=null;
	
	
	public XideoSubscriptionPage() {
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
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(2000);
		JSONObject response;
		JSONArray channels = null; 
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
			
		
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(1000);
		
		for(int i=0;i<channels.length();i++)
		{solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		JSONObject currChannel;
		try {
			currChannel = channels.getJSONObject(i);
			solo.sleep(500);
			String currChannelId;
			assertTrue(solo.searchText(currChannel.getString("title")));
			solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
			currChannelId=currChannel.getString("@id");
			JSONArray showContent=GetShowContent.getInstance().getShowContent(currChannelId);
			if(showContent!=null)
			for(int j=0;j<showContent.length();j++)
			{solo.sleep(500);
				try {
					JSONObject show=showContent.getJSONObject(j);
					assertTrue(solo.searchText(show.getString("title")));
					solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
					} catch (JSONException e) {
					Log.e("test_Subscrption_Title", e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		} catch (JSONException e1) {
			Log.e("test_Subscrption_Title", e1.getLocalizedMessage());
		}
			
		}
		//"1792275"
		//1792276
	}
	
}


