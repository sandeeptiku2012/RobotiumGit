package com.comcast.xidio.testCases.navigationBar;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xidio.core.common.GetCatagoryLists;
import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.core.XidioApplication;

public class XidioNavigationRightToLeft extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;

	public XidioNavigationRightToLeft() {
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

	public void testXideoHomeActivityChangeTitle() 
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
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		assertTrue(solo.waitForText(TestConstants.SETTINGS));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		assertTrue(solo.waitForText(TestConstants.SUBSCRIPTIONS));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		assertTrue(solo.waitForText(TestConstants.HOME));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		assertTrue(solo.waitForText(TestConstants.SEARCH));

	}
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}
}
