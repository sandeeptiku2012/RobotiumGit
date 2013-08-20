package com.comcast.xideo.testCases.home.featured;

import org.json.JSONArray;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoDetailsActivityChangeTitle extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;

	public XideoDetailsActivityChangeTitle() {
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

	public void testDetailsActivityChangeTitle()
	{
		String channelTitle = null;
		solo.sleep(500);
		solo.sleep(300);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(200);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		
		JSONArray featuredJsonArray = GetCatagoryLists.getInstance().getFeaturedList();
		if(featuredJsonArray!=null && featuredJsonArray.length()>0)
		{
			for (int i = 0; i < featuredJsonArray.length(); i++) 
			{
				JSONObject currentChannel = null;
				try {
					currentChannel = featuredJsonArray.getJSONObject(i);
					channelTitle = currentChannel.getString(TestConstants.TITLE);
				} catch (Exception e) {
					assertTrue(false);
				}
				if(!currentChannel.has("category"))
					continue;
				solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
				solo.sleep(500);
				assertTrue(solo.waitForActivity(TestConstants.DETAILS_ACTIVITY));
				assertTrue(solo.waitForText(channelTitle));
				solo.sendKey(KeyEvent.KEYCODE_BACK);
				assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
				solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
	
			}
		}
	}

}
