package com.comcast.xideo.testCases.home.upNext;

import org.json.JSONArray;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.core.XidioApplication;

public class XideoHomeActivityChange extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;

	public XideoHomeActivityChange() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception
	{
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(XidioApplication.getLastLoggedInUser(), XidioApplication.getLastSessionId());
		super.setUp();
	}

	public void testXideoHomeActivityChange() 
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(1000);
		solo.sleep(500);
		
		JSONArray upNextArray = GetCatagoryLists.getInstance().getUpNextList();
		if(upNextArray !=null && upNextArray.length()>0)
		{
			for (int i = 0; i < upNextArray.length(); i++) 
			{
				solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
				assertTrue(solo.waitForActivity(TestConstants.VIDEOPLAYER_ACTIVITY));
				solo.sleep(500);
				solo.sendKey(KeyEvent.KEYCODE_BACK);
				solo.sleep(500);
				solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
				solo.sleep(500);
	
			}
		}

	}
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
