package com.comcast.xidio.testCases.settings;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.comcast.xidio.model.GetLoginResponse;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XidioSettingsDisplayButton extends ActivityInstrumentationTestCase2<FirstRun>{

	private Solo solo;	
	
	public XidioSettingsDisplayButton() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testXidioSettingsDisplayButton() 
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		GetLoginResponse.getInstance().getLoginResponse(TestConstants.USERNAME, TestConstants.PASSWORD);
		
		solo.sleep(TestConstants.SLEEP_TIME_5000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		assertTrue(solo.searchText(TestConstants.SETTINGS));
		solo.sleep(TestConstants.SLEEP_TIME_1000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(TestConstants.SLEEP_TIME_500);
		assertTrue(solo.searchText("DISPLAY"));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.sleep(TestConstants.SLEEP_TIME_500);
	
	}
	
	@Override
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
