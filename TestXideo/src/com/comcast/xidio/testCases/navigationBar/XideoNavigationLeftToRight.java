package com.comcast.xidio.testCases.navigationBar;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xidio.core.common.GetCatagoryLists;
import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
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

	public void testXideoNavigationLeftToRight()
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_1000);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		assertTrue(solo.waitForText("SEARCH"));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		assertTrue(solo.waitForText("HOME"));
		solo.sleep(TestConstants.SLEEP_TIME_500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		assertTrue(solo.waitForText("SUBSCRIPTIONS"));
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		assertTrue(solo.waitForText(TestConstants.SETTINGS));
		solo.sleep(TestConstants.SLEEP_TIME_500);
	}
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}
}
