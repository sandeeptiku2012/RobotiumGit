package com.comcast.xideo.testCases.home.featured;

import org.json.JSONArray;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoHomeActivityFeaturedListTitle extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;

	public XideoHomeActivityFeaturedListTitle() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception 
	{
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(), getActivity().getSessionId());
		super.setUp();
		
	}

	public void testHomeActivityFeaturedListTitle()
	{
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(200);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		try {
			JSONArray featuredJsonArray = GetCatagoryLists.getInstance().getFeaturedList();
			if(featuredJsonArray!=null && featuredJsonArray.length()>0)
			{
			
				for (int count = 0; count < featuredJsonArray.length(); count++) 
				{
					JSONObject currentChannel = featuredJsonArray.getJSONObject(count);
	
					if ((currentChannel.has("productGroup") || !currentChannel.has("category")) && !currentChannel.has("asset"))
							continue;
					String channelTitle =null;
					if(currentChannel.has("title"))
					{
						channelTitle = currentChannel.getString("title");
					}
					if(channelTitle!=null && channelTitle.length()>0)
					{
						solo.sleep(50);
						assertTrue(solo.waitForText(channelTitle));
					}
					
					solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				}
			
			}
		} catch (Exception e) 
		{
			Log.e(this.getClass().getCanonicalName(), "Failed to complete the tset XideoHomeActivityFeaturedListTitle " , e);
		}
		solo.sleep(2000);
	}
}
