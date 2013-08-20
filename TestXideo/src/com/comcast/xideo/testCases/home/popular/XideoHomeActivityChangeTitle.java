package com.comcast.xideo.testCases.home.popular;

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

public class XideoHomeActivityChangeTitle extends ActivityInstrumentationTestCase2<MainActivity> 
{
	private Solo solo;
			
	public XideoHomeActivityChangeTitle() {
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
	

	public void testHomeActivityChangeTitle()
	{
		String channelTitle = null;
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);

		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);

		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		
		JSONArray popularJsonArray =   GetCatagoryLists.getInstance().getPopularList();
		if(popularJsonArray!=null && popularJsonArray.length()>0)
		{
			for (int i = 0; i < popularJsonArray.length(); i++)
			{
				JSONObject currentChannel = null;
				try {
					currentChannel = popularJsonArray.getJSONObject(i);
					if (!currentChannel.has("category"))
						continue;
					channelTitle = currentChannel.getString(TestConstants.TITLE);
				} catch (Exception e)
				{
					Log.e(this.getClass().getCanonicalName(), "Failed to complete the tset XideoHomeActivityChangeTitle " , e);
				}
	
				solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
				assertTrue(solo.waitForText(channelTitle));
				solo.sendKey(KeyEvent.KEYCODE_BACK);
				assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
				solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
			}
		}
	}

}
