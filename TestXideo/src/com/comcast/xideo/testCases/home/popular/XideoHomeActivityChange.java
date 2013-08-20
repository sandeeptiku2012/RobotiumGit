package com.comcast.xideo.testCases.home.popular;

import org.json.JSONArray;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoHomeActivityChange extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;

	public XideoHomeActivityChange (){
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

	public void testHomeActivityChange() 
	{
		solo.sleep(500);
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);

		solo.sleep(200);
		JSONArray popularJsonArray =   GetCatagoryLists.getInstance().getPopularList();
		
		if(popularJsonArray!=null && popularJsonArray.length()>0)
		{
			for (int i = 0; i < popularJsonArray.length(); i++) 
			{
				solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
				assertTrue(solo.waitForActivity(TestConstants.DETAILS_ACTIVITY));
				solo.sleep(200);
				solo.sendKey(KeyEvent.KEYCODE_BACK);
				solo.sleep(200);
				solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				solo.sleep(500);
	
			}
		}

	}

}
