package com.comcast.xideo.testCases.home.featured;

import org.json.JSONArray;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoDetailsActivityChange extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;

	public XideoDetailsActivityChange() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(), getActivity().getSessionId());
		super.setUp();
	}

	public void testDetailsActivityChange() 
	{
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		
		JSONArray featuredJsonArray = GetCatagoryLists.getInstance().getFeaturedList();
		if(featuredJsonArray!=null && featuredJsonArray.length()>0)
		{
			for (int i = 0; i < featuredJsonArray.length(); i++) 
			{
	
				solo.sleep(500);
				solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
				assertTrue(solo.waitForActivity(TestConstants.DETAILS_ACTIVITY));
				solo.sleep(500);
				solo.sendKey(KeyEvent.KEYCODE_BACK);
				assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
				solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
			}
		}	

	}

}
