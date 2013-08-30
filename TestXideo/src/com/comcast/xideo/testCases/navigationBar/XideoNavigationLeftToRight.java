package com.comcast.xideo.testCases.navigationBar;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.core.XidioApplication;

public class XideoNavigationLeftToRight extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;

	public XideoNavigationLeftToRight() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(), getActivity());
		solo = GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(XidioApplication.getLastLoggedInUser(), XidioApplication.getLastSessionId());
		super.setUp();
	}

	public void testNavigationLeftToRight()
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(1000);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(200);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		assertTrue(solo.waitForText("SEARCH"));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		assertTrue(solo.waitForText("HOME"));
		solo.sleep(200);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		assertTrue(solo.waitForText("SUBSCRIPTIONS"));
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		assertTrue(solo.waitForText(TestConstants.SETTINGS));
		solo.sleep(500);
	}

}
