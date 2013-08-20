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

public class XideoHomeActivityPopularTitle extends ActivityInstrumentationTestCase2<MainActivity> 
{
	private Solo solo;
	
	public XideoHomeActivityPopularTitle() {
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
	
	public void testHomePopularTitle() 
	{
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(200);
		solo.sleep(200);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(200);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(200);
		
		try {
			JSONArray popularJsonArray =   GetCatagoryLists.getInstance().getPopularList();
			
			if(popularJsonArray!=null && popularJsonArray.length()>0)
			{
				for (int size = 0; size < popularJsonArray.length(); size++) 
				{
					JSONObject currentChannel = popularJsonArray.getJSONObject(size);
	
					if (!currentChannel.has("category"))
						continue;
	
					String ChannelTitle = currentChannel.getString(TestConstants.TITLE);
	
					assertTrue(solo.waitForText(ChannelTitle));
					solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				}
			}
		} catch (Exception e) 
		{
			Log.e(this.getClass().getCanonicalName(), "Failed to complete the tset XideoHomeActivityPopularTitle " , e);
		}

	}

}
