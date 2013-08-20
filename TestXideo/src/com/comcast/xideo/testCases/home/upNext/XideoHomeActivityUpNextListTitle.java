package com.comcast.xideo.testCases.home.upNext;

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

public class XideoHomeActivityUpNextListTitle extends ActivityInstrumentationTestCase2<MainActivity> 
{
	private Solo solo;

	public XideoHomeActivityUpNextListTitle() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(), getActivity().getSessionId());
		super.setUp();
	}

 public void testHomeUpNextListTitle()
	{
		String ChannelTitle = null;
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);

		JSONArray upNextArray = GetCatagoryLists.getInstance().getUpNextList();
		if(upNextArray !=null && upNextArray.length()>0)
		{
			for (int i = 0; i < upNextArray.length(); i++)
			{
				JSONObject currentChannel = null;
				try {
					currentChannel = upNextArray.getJSONObject(i);
					ChannelTitle = currentChannel.getString(TestConstants.TITLE);
	
				} catch (Exception e)
				{
					Log.e(this.getClass().getCanonicalName(), "Failed to get title from XideoHomeActivityUpNextListTitle.java " , e);
				}
				assertTrue(solo.waitForText(ChannelTitle));
				solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				solo.sleep(500);
	
			}
		}
	}
}
