package com.comcast.xideo.testCases.navigationBar;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoNavigationRightToLeft extends ActivityInstrumentationTestCase2<MainActivity>
{
	private Solo solo;

	public XideoNavigationRightToLeft() {
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

	public void testNavigationRightToLeft() 
	{
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(200);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(200);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(200);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(200);
		assertTrue(solo.waitForText(TestConstants.SETTINGS));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(200);
		assertTrue(solo.waitForText(TestConstants.SUBSCRIPTIONS));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(200);
		assertTrue(solo.waitForText(TestConstants.HOME));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(200);
		assertTrue(solo.waitForText(TestConstants.SEARCH));

	}

}
